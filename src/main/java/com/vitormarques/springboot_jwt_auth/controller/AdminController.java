package com.vitormarques.springboot_jwt_auth.controller;

import com.vitormarques.springboot_jwt_auth.dto.UpdateRoleRequest;
import com.vitormarques.springboot_jwt_auth.service.UserRoleUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Administrative endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final UserRoleUpdateService userRoleUpdateService;

    public AdminController(UserRoleUpdateService userRoleUpdateService) {
        this.userRoleUpdateService = userRoleUpdateService;
    }

    @PutMapping("/users/roles")
    @Operation(summary = "Update user roles (Admin only)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserRoles(@Valid @RequestBody UpdateRoleRequest request) {
        userRoleUpdateService.updateUserRoles(request.getUsername(), request.getRoleNames());
        return ResponseEntity.ok().build();
    }
}