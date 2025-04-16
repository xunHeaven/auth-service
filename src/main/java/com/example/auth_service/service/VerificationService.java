package com.example.auth_service.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    private static final String VERIFICATION_CODE_PREFIX = "verification:code:";
    private static final long VERIFICATION_CODE_EXPIRE_MINUTES = 5;

    public VerificationService(RedisTemplate<String, String> redisTemplate,
                               EmailService emailService) {
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
    }

    public void sendVerificationCode(String emailOrPhone) {
        String code = generateRandomCode(6);
        String key = VERIFICATION_CODE_PREFIX + emailOrPhone;

        redisTemplate.opsForValue().set(
                key,
                code,
                Duration.ofMinutes(VERIFICATION_CODE_EXPIRE_MINUTES)
        );

        // 这里简化处理，只实现邮件验证码
        emailService.sendVerificationEmail(emailOrPhone, code);
    }

    public boolean verifyCode(String emailOrPhone, String code) {
        String key = VERIFICATION_CODE_PREFIX + emailOrPhone;
        String storedCode = redisTemplate.opsForValue().get(key);

        if (storedCode == null) {
            return false;
        }

        return storedCode.equals(code);
    }

    private String generateRandomCode(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }
}
