package com.vitormarques.springboot_jwt_auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private UUID id;
    private String username;
    private String email;
}