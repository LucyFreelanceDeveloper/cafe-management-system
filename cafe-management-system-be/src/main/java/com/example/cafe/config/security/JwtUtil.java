package com.example.cafe.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private final String secret = "myKey";

    private static final int EXPIRATION_TIME_IN_MS = 1000 * 60 * 60 * 10;

    public String extractUsername(@NotNull final String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(@NotNull final String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(@NotNull final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(@NotNull final String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(@NotNull final String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(@NotNull final String username, @NotNull final String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return createToken(claims, username);
    }

    private String createToken(@NotNull final Map<String, Object> claims, @NotNull final String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_IN_MS))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public Boolean validateToken(@NotNull final String token, @NotNull final UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}