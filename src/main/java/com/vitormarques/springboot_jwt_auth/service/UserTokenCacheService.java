package com.vitormarques.springboot_jwt_auth.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserTokenCacheService {

    private final ConcurrentHashMap<String, String> userTokenMap = new ConcurrentHashMap<>();

    public void storeToken(String username, String token) {
        userTokenMap.put(username, token);
    }

    public String getToken(String username) {
        return userTokenMap.get(username);
    }

    public void removeToken(String username) {
        userTokenMap.remove(username);
    }
}