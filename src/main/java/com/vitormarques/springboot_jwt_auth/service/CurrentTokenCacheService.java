package com.vitormarques.springboot_jwt_auth.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CurrentTokenCacheService {

    private final Map<String, String> userTokenCache = new ConcurrentHashMap<>();

    public void storeToken(String username, String token) {
        userTokenCache.put(username, token);
    }

    public Optional<String> getCurrentToken(String username) {
        return Optional.ofNullable(userTokenCache.get(username));
    }

    public void removeToken(String username) {
        userTokenCache.remove(username);
    }
}