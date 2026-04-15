package com.vitormarques.springboot_jwt_auth.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryTokenBlacklistService implements TokenBlacklistService {

    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    @Override
    public void revokeToken(String token) {
        blacklist.add(token);
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return blacklist.contains(token);
    }
}