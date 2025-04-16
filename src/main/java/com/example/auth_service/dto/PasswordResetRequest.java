package com.example.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String verificationCode;

    @NotBlank
    @Size(min = 6, max = 40)
    private String newPassword;
}