package com.landminesoft.lms.service;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.entity.*;
import com.landminesoft.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // ── Student Register ──
    public RegisterResponseDTO registerStudent(StudentRegisterDTO dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        String rollNumber = generateRollNumber(dto.getBranch(), dto.getEnrollmentYear());

        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .branch(dto.getBranch())
                .enrollmentYear(dto.getEnrollmentYear())
                .rollNumber(rollNumber)
                .semester(1)
                .build();
        Student saved = studentRepository.save(student);
        return RegisterResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role("STUDENT")
                .message("Registration successful. Please login.")
                .build();
    }

    // ── Faculty Register ──
    public RegisterResponseDTO registerFaculty(FacultyRegisterDTO dto) {
        if (facultyRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        FacultyPersonal faculty = FacultyPersonal.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .department(dto.getDepartment())
                .designation(dto.getDesignation())
                .build();
        FacultyPersonal saved = facultyRepository.save(faculty);
        return RegisterResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role("FACULTY")
                .message("Registration successful. Please login.")
                .build();
    }

    // ── Admin Register ──
    public RegisterResponseDTO registerAdmin(AdminRegisterDTO dto) {
        if (adminRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        Admin admin = Admin.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
        Admin saved = adminRepository.save(admin);
        return RegisterResponseDTO.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role("ADMIN")
                .message("Registration successful. Please login.")
                .build();
    }

    // ── Student Login ──
    public JwtResponseDTO loginStudent(LoginDTO dto) {
        Student student = studentRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), student.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
        // JWT will be added in Week 3 — returning user info for now
        return JwtResponseDTO.builder()
                .token("JWT_TOKEN_COMING_IN_WEEK_3")
                .type("Bearer")
                .userId(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .role("STUDENT")
                .build();
    }

    // ── Faculty Login ──
    public JwtResponseDTO loginFaculty(LoginDTO dto) {
        FacultyPersonal faculty = facultyRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), faculty.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
        return JwtResponseDTO.builder()
                .token("JWT_TOKEN_COMING_IN_WEEK_3")
                .type("Bearer")
                .userId(faculty.getId())
                .email(faculty.getEmail())
                .name(faculty.getName())
                .role("FACULTY")
                .build();
    }

    // ── Admin Login ──
    public JwtResponseDTO loginAdmin(LoginDTO dto) {
        Admin admin = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), admin.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
        return JwtResponseDTO.builder()
                .token("JWT_TOKEN_COMING_IN_WEEK_3")
                .type("Bearer")
                .userId(admin.getId())
                .email(admin.getEmail())
                .name(admin.getName())
                .role("ADMIN")
                .build();
    }
    
    // ── Roll Number Generator ──
    private String generateRollNumber(String branch, Integer enrollmentYear) {
        long count = studentRepository.countByBranchAndEnrollmentYear(branch, enrollmentYear);
        return branch.toUpperCase() + enrollmentYear + String.format("%03d", count + 1);
    }
}