package com.vitormarques.springboot_jwt_auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Set;

@Data
public class UpdateRoleRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Role list cannot be null")
    private Set<String> roleNames;
}