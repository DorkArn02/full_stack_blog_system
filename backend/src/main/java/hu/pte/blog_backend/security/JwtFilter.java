package hu.pte.blog_backend.security;

import hu.pte.blog_backend.services.CustomUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService customUserDetails;

    private final JwtUtil jwtUtil;

    public JwtFilter(CustomUserDetailsService customUserDetails, JwtUtil jwtUtil) {
        this.customUserDetails = customUserDetails;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (!request.getRequestURI().contains("auth")) {
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.substring(7);
                String msg = "";
                try {
                    username = jwtUtil.getUsernameFromToken(token);
                } catch (SignatureException e) {
                    msg = "Invalid JWT signature";
                } catch (MalformedJwtException e) {
                    msg = "Invalid JWT token";
                }catch (ExpiredJwtException e){
                    msg = "Expired JWT token";
                }catch (UnsupportedJwtException e){
                    msg = "Unsupported JWT token";
                }catch (IllegalArgumentException e){
                    msg = "JWT claims string is empty";
                }
                if(!msg.isEmpty()){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
            if (null != username && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customUserDetails.loadUserByUsername(username);
                if (jwtUtil.validateJwtToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken
                            authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null,
                            userDetails.getAuthorities());
                    authenticationToken.setDetails(new
                            WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Objects.equals(request.getMethod(), HttpMethod.GET.name()) && request.getRequestURI().equals("/api/posts/");
    }
}
