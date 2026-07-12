package com.vitormarques.springboot_jwt_auth.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

@Component
public class JwtKeyManager {

    private final SecretKey secretKey;

    public JwtKeyManager(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}