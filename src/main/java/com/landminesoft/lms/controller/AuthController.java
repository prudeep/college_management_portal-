package com.landminesoft.lms.controller;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.service.AuthService;
import com.landminesoft.lms.service.PasswordResetService;
import com.landminesoft.lms.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "APIs for registration, login and password management")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;
    private final ProfileService profileService;

    @Operation(summary = "Register a new student")
    @PostMapping("/student/register")
    public ResponseEntity<RegisterResponseDTO> registerStudent(
            @Valid @RequestBody StudentRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerStudent(dto));
    }

    @Operation(summary = "Student login - returns JWT token")
    @PostMapping("/student/login")
    public ResponseEntity<JwtResponseDTO> loginStudent(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginStudent(dto));
    }

    @Operation(summary = "Register a new faculty member")
    @PostMapping("/faculty/register")
    public ResponseEntity<RegisterResponseDTO> registerFaculty(
            @Valid @RequestBody FacultyRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerFaculty(dto));
    }

    @Operation(summary = "Faculty login - returns JWT token")
    @PostMapping("/faculty/login")
    public ResponseEntity<JwtResponseDTO> loginFaculty(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginFaculty(dto));
    }

    @Operation(summary = "Register a new admin - SuperAdmin only")
    @PostMapping("/admin/register")
    public ResponseEntity<RegisterResponseDTO> registerAdmin(
            @Valid @RequestBody AdminRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerAdmin(dto));
    }

    @Operation(summary = "Admin login - returns JWT token")
    @PostMapping("/admin/login")
    public ResponseEntity<JwtResponseDTO> loginAdmin(
            @Valid @RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginAdmin(dto));
    }

    @Operation(summary = "Send password reset link to email")
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordDTO dto) {
        return ResponseEntity.ok(passwordResetService.forgotPassword(dto));
    }

    @Operation(summary = "Reset password using token from email")
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordDTO dto) {
        return ResponseEntity.ok(passwordResetService.resetPassword(dto));
    }

    @Operation(summary = "Change password - JWT required")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordDTO dto) {
        return ResponseEntity.ok(profileService.changePassword(
                authentication.getName(), dto));
    }
}