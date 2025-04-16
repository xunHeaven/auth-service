package com.example.auth_service.controller;

import com.example.authservice.dto.*;
import com.example.authservice.service.AuthService;
import com.example.authservice.service.PasswordService;
import com.example.authservice.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final VerificationService verificationService;
    private final PasswordService passwordService;

    public AuthController(AuthService authService,
                          VerificationService verificationService,
                          PasswordService passwordService) {
        this.authService = authService;
        this.verificationService = verificationService;
        this.passwordService = passwordService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<Void> sendVerificationCode(@RequestParam String email) {
        verificationService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        passwordService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        passwordService.resetPassword(
                request.getEmail(),
                request.getVerificationCode(),
                request.getNewPassword()
        );
        return ResponseEntity.ok().build();
    }
}