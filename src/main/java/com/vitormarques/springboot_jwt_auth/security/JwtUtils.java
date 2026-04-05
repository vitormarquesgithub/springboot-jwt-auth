package com.vitormarques.springboot_jwt_auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final JwtKeyManager jwtKeyManager;
    private final long expiration;
    private final long refreshExpiration;

    public JwtUtils(JwtKeyManager jwtKeyManager,
                    @Value("${jwt.expiration}") long expiration,
                    @Value("${jwt.refresh-expiration}") long refreshExpiration) {
        this.jwtKeyManager = jwtKeyManager;
        this.expiration = expiration;
        this.refreshExpiration = refreshExpiration;
    }

    public String generateAccessToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return buildToken(userDetails, extraClaims, expiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, new HashMap<>(), refreshExpiration);
    }

    private String buildToken(UserDetails userDetails, Map<String, Object> claims, long expirationMillis) {
        return Jwts.builder()
                .claims(claims) // Método atualizado: 'claims()'
                .subject(userDetails.getUsername()) // Método atualizado: 'subject()'
                .issuedAt(new Date()) // Método atualizado: 'issuedAt()'
                .expiration(new Date(System.currentTimeMillis() + expirationMillis)) // Método atualizado: 'expiration()'
                .signWith(jwtKeyManager.getSecretKey(), Jwts.SIG.HS256) // Método atualizado: 'Jwts.SIG.HS256'
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtKeyManager.getSecretKey()) // Método atualizado: 'verifyWith()'
                .build()
                .parseSignedClaims(token) // Método atualizado: 'parseSignedClaims()'
                .getPayload(); // Método atualizado: 'getPayload()'
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}