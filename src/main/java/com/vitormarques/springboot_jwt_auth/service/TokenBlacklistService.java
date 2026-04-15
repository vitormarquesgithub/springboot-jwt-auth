package com.vitormarques.springboot_jwt_auth.service;

public interface TokenBlacklistService {
    void revokeToken(String token);
    boolean isTokenRevoked(String token);
}