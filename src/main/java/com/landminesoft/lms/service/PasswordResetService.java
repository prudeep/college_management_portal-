package com.landminesoft.lms.service;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.entity.Admin;
import com.landminesoft.lms.entity.FacultyPersonal;
import com.landminesoft.lms.entity.PasswordResetToken;
import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public String forgotPassword(ForgotPasswordDTO dto) {
        String email = dto.getEmail();
        String userType = findUserType(email);

        if (userType == null) {
            return "If this email exists, a reset link has been sent.";
        }

        tokenRepository.deleteByEmail(email);

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .userType(userType)
                .expiryTime(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        tokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(email, token);

        return "Password reset link sent to your email.";
    }

    @Transactional
    public String resetPassword(ResetPasswordDTO dto) {
        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        if (resetToken.getUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired. Please request a new one.");
        }

        String hashedPassword = passwordEncoder.encode(dto.getNewPassword());
        String email = resetToken.getEmail();

        switch (resetToken.getUserType()) {
            case "STUDENT" -> {
                Student student = studentRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                student.setPasswordHash(hashedPassword);
                studentRepository.save(student);
            }
            case "FACULTY" -> {
                FacultyPersonal faculty = facultyRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                faculty.setPasswordHash(hashedPassword);
                facultyRepository.save(faculty);
            }
            case "ADMIN" -> {
                Admin admin = adminRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                admin.setPasswordHash(hashedPassword);
                adminRepository.save(admin);
            }
        }

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return "Password reset successfully. Please login with your new password.";
    }

    private String findUserType(String email) {
        if (studentRepository.existsByEmail(email)) return "STUDENT";
        if (facultyRepository.existsByEmail(email)) return "FACULTY";
        if (adminRepository.existsByEmail(email)) return "ADMIN";
        return null;
    }
}