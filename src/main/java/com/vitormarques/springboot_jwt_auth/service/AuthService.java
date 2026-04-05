package com.vitormarques.springboot_jwt_auth.service;

import com.vitormarques.springboot_jwt_auth.dto.AuthRequest;
import com.vitormarques.springboot_jwt_auth.dto.AuthResponse;
import com.vitormarques.springboot_jwt_auth.dto.RegisterRequest;
import com.vitormarques.springboot_jwt_auth.dto.RegisterResponse;
import com.vitormarques.springboot_jwt_auth.entity.Role;
import com.vitormarques.springboot_jwt_auth.entity.User;
import com.vitormarques.springboot_jwt_auth.repository.RoleRepository;
import com.vitormarques.springboot_jwt_auth.repository.UserRepository;
import com.vitormarques.springboot_jwt_auth.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // In-memory blacklist for tokens (use Redis in production)
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    public AuthService(AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils,
                       UserDetailsService userDetailsService,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId().toString());
        claims.put("roles", user.getAuthorities());
        claims.put("iss", "springboot-jwt-auth");
        claims.put("aud", "jwt-api");

        String accessToken = jwtUtils.generateAccessToken(userDetails, claims);
        String refreshToken = jwtUtils.generateRefreshToken(userDetails);
        return new AuthResponse(accessToken, refreshToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role USER not found"));
        user.setRoles(new HashSet<>(Set.of(userRole)));

        User savedUser = userRepository.save(user);
        return new RegisterResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        String username = jwtUtils.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtUtils.isTokenValid(refreshToken, userDetails) && !isTokenRevoked(refreshToken)) {
            Map<String, Object> claims = new HashMap<>();
            String newAccessToken = jwtUtils.generateAccessToken(userDetails, claims);
            return new AuthResponse(newAccessToken, refreshToken);
        }
        throw new RuntimeException("Invalid or revoked refresh token");
    }

    public void logout(String accessToken) {
        tokenBlacklist.add(accessToken);
    }

    public boolean isTokenRevoked(String token) {
        return tokenBlacklist.contains(token);
    }
}