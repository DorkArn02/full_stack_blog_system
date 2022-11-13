package hu.pte.blog_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.KeyPair;
import java.util.Date;

@Service
public class JwtUtil {
    private final KeyPair keys = Keys.keyPairFor(SignatureAlgorithm.RS512);

    public String generateToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
                .setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 300000)) // Five minutes expiry time
                .signWith(keys.getPrivate()).compact();
    }

    public Boolean validateJwtToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);

        if(username == null){
            return false;
        }

        Claims claims = Jwts.parserBuilder().setSigningKey(keys.getPublic()).build().parseClaimsJws(token).getBody();
        boolean isTokenExpired = claims.getExpiration().before(new Date()); // If it is not expired then it will be false

        if(isTokenExpired){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT token is expired.");
        }

        return username.equals(userDetails.getUsername());
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(keys.getPublic()).build().parseClaimsJws(token).getBody();
            return claims.getSubject();
        }catch (MalformedJwtException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong JWT token provided.");
        }catch (SignatureException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong JWT Signature");
        }catch (Exception e){
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
