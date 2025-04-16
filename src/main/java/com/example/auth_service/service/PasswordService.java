package com.example.auth_service.service;

import com.example.authservice.entity.User;
import com.example.authservice.exception.AuthException;
import com.example.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationService verificationService;

    public void requestPasswordReset(String email) {
        if (!userRepository.existsByEmail(email)) {
            // 出于安全考虑，不提示用户是否存在
            return;
        }

        verificationService.sendVerificationCode(email);
    }

    public void resetPassword(String email, String verificationCode, String newPassword) {
        if (!verificationService.verifyCode(email, verificationCode)) {
            throw new AuthException("Invalid verification code");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
