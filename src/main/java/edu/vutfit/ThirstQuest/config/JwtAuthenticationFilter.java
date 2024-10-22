package edu.vutfit.ThirstQuest.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.spec.SecretKeySpec;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String bearerPrefix = "Bearer ";
    private static final int bearerPrefixLength = bearerPrefix.length();

    private final UserDetailsService userDetailsService;
    private final String jwtSecret = "your_very_long_jwt_secret_key_that_is_at_least_32_bytes_long"; // Replace with your secret key

    public JwtAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(bearerPrefix) || SecurityContextHolder.getContext().getAuthentication() != null) {
            chain.doFilter(request, response);
            return;
        }

        String authToken = header.substring(bearerPrefixLength);
        var signingKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(authToken)
                    .getPayload();

            String username = claims.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            logger.error("An error occurred while parsing JWT token", e);
        }

        chain.doFilter(request, response);
    }
}
