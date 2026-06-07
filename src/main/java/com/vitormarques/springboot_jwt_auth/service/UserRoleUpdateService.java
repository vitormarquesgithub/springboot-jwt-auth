package com.vitormarques.springboot_jwt_auth.service;

import com.vitormarques.springboot_jwt_auth.entity.Role;
import com.vitormarques.springboot_jwt_auth.entity.User;
import com.vitormarques.springboot_jwt_auth.repository.RoleRepository;
import com.vitormarques.springboot_jwt_auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserRoleUpdateService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final CurrentTokenCacheService currentTokenCacheService;

    public UserRoleUpdateService(UserRepository userRepository,
                                 RoleRepository roleRepository,
                                 TokenBlacklistService tokenBlacklistService,
                                 CurrentTokenCacheService currentTokenCacheService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenBlacklistService = tokenBlacklistService;
        this.currentTokenCacheService = currentTokenCacheService;
    }

    @Transactional
    public void updateUserRoles(String username, Set<String> newRoleNames) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Set<Role> newRoles = newRoleNames.stream()
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(newRoles);
        userRepository.save(user);

        currentTokenCacheService.getCurrentToken(username).ifPresent(token -> {
            tokenBlacklistService.revokeToken(token);
            currentTokenCacheService.removeToken(username);
        });
    }
}