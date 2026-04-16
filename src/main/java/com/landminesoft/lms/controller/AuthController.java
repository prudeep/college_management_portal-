package com.landminesoft.lms.controller;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ── Student APIs ──
    @PostMapping("/student/register")
    public ResponseEntity<RegisterResponseDTO> registerStudent(@RequestBody StudentRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerStudent(dto));
    }

    @PostMapping("/student/login")
    public ResponseEntity<JwtResponseDTO> loginStudent(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginStudent(dto));
    }

    // ── Faculty APIs ──
    @PostMapping("/faculty/register")
    public ResponseEntity<RegisterResponseDTO> registerFaculty(@RequestBody FacultyRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerFaculty(dto));
    }

    @PostMapping("/faculty/login")
    public ResponseEntity<JwtResponseDTO> loginFaculty(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginFaculty(dto));
    }

    // ── Admin APIs ──
    @PostMapping("/admin/register")
    public ResponseEntity<RegisterResponseDTO> registerAdmin(@RequestBody AdminRegisterDTO dto) {
        return ResponseEntity.ok(authService.registerAdmin(dto));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<JwtResponseDTO> loginAdmin(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(authService.loginAdmin(dto));
    }
}