package com.landminesoft.lms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("LMS Portal - Password Reset Request");
            message.setText(
                "Hello,\n\n" +
                "You requested a password reset for your LMS account.\n\n" +
                "Your password reset token is:\n" +
                token + "\n\n" +
                "Use this token at: POST /api/auth/reset-password\n\n" +
                "This token expires in 15 minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\nLandmine Soft LMS Team"
            );
            mailSender.send(message);
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", toEmail, e);
            throw new RuntimeException("Failed to send reset email. Please try again.");
        }
    }
}