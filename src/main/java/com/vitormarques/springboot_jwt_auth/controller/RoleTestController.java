package com.vitormarques.springboot_jwt_auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@Tag(name = "Role Test", description = "Endpoints to demonstrate role-based access control")
@SecurityRequirement(name = "Bearer Authentication")
public class RoleTestController {

    @GetMapping("/user")
    @Operation(summary = "User access", description = "Accessible by ROLE_USER or ROLE_ADMIN")
    @ApiResponse(responseCode = "200", description = "Access granted")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public String userAccess() {
        return "User content – accessible by USER or ADMIN";
    }

    @GetMapping("/admin")
    @Operation(summary = "Admin access", description = "Accessible only by ROLE_ADMIN")
    @ApiResponse(responseCode = "200", description = "Access granted")
    @ApiResponse(responseCode = "403", description = "Forbidden – insufficient role")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin content – only ADMIN";
    }
}