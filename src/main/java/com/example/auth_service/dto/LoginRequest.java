package com.example.auth_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
