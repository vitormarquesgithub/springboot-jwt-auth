package com.vitormarques.springboot_jwt_auth.controller;

import com.vitormarques.springboot_jwt_auth.dto.AuthRequest;
import com.vitormarques.springboot_jwt_auth.dto.AuthResponse;
import com.vitormarques.springboot_jwt_auth.dto.RefreshTokenRequest;
import com.vitormarques.springboot_jwt_auth.dto.RegisterRequest;
import com.vitormarques.springboot_jwt_auth.dto.RegisterResponse;
import com.vitormarques.springboot_jwt_auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for login, registration, token refresh and logout")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Returns access token and refresh token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful authentication"),
        @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
    })
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a user with ROLE_USER by default")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error (weak password, duplicate username/email)")
    })
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Provide valid refresh token to get a new access token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "New access token issued"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request.getRefreshToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revokes the access token (adds to blacklist)")
    @ApiResponse(responseCode = "200", description = "Token revoked successfully")
    public ResponseEntity<Void> logout(
        @Parameter(description = "Bearer token to revoke", example = "Bearer eyJhbGciOiJIUzI1...")
        @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok().build();
    }
}