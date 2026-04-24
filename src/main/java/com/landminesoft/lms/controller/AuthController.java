package com.landminesoft.lms.controller;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.service.AuthService;
import com.landminesoft.lms.service.PasswordResetService;
import com.landminesoft.lms.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final ProfileService profileService;

    @PostMapping("/student/register")
    public ResponseEntity<RegisterResponseDTO> registerStudent(
            @Valid @RequestBody StudentRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerStudent(dto));
    }

    @PostMapping("/student/login")
    public ResponseEntity<JwtResponseDTO> loginStudent(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginStudent(dto));
    }

    @PostMapping("/faculty/register")
    public ResponseEntity<RegisterResponseDTO> registerFaculty(
            @Valid @RequestBody FacultyRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerFaculty(dto));
    }

    @PostMapping("/faculty/login")
    public ResponseEntity<JwtResponseDTO> loginFaculty(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginFaculty(dto));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<RegisterResponseDTO> registerAdmin(
            @Valid @RequestBody AdminRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerAdmin(dto));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<JwtResponseDTO> loginAdmin(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginAdmin(dto));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordDTO dto) {
        return ResponseEntity.ok(passwordResetService.forgotPassword(dto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto) {
        return ResponseEntity.ok(passwordResetService.resetPassword(dto));
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordDTO dto) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.changePassword(email, dto));
    }
}