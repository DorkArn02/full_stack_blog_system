package hu.pte.blog_backend.security;

import io.jsonwebtoken.*;
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
                .setExpiration(new Date(System.currentTimeMillis() + 3000000)) // 50 minutes expiry time
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
        Claims claims = Jwts.parserBuilder().setSigningKey(keys.getPublic()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();

    }

}
