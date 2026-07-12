package com.vitormarques.springboot_jwt_auth.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private UUID id;
    private String username;
    private String email;
}