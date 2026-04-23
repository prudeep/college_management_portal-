package com.landminesoft.lms.service;

import com.landminesoft.lms.config.JwtUtils;
import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.entity.*;
import com.landminesoft.lms.exception.*;
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
    private final JwtUtils jwtUtils;

    public RegisterResponseDTO registerStudent(StudentRegisterDTO dto) {
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Student with email already exists: " + dto.getEmail());
        }
        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .branch(dto.getBranch())
                .enrollmentYear(dto.getEnrollmentYear())
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

    public RegisterResponseDTO registerFaculty(FacultyRegisterDTO dto) {
        if (facultyRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Faculty with email already exists: " + dto.getEmail());
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

    public RegisterResponseDTO registerAdmin(AdminRegisterDTO dto) {
        if (adminRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("Admin with email already exists: " + dto.getEmail());
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

    public JwtResponseDTO loginStudent(LoginDTO dto) {
        Student student = studentRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), student.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtUtils.generateToken(student.getId(), student.getEmail(), "STUDENT");
        return JwtResponseDTO.builder()
                .token(token)
                .userId(student.getId())
                .email(student.getEmail())
                .name(student.getName())
                .role("STUDENT")
                .build();
    }

    public JwtResponseDTO loginFaculty(LoginDTO dto) {
        FacultyPersonal faculty = facultyRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), faculty.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtUtils.generateToken(faculty.getId(), faculty.getEmail(), "FACULTY");
        return JwtResponseDTO.builder()
                .token(token)
                .userId(faculty.getId())
                .email(faculty.getEmail())
                .name(faculty.getName())
                .role("FACULTY")
                .build();
    }

    public JwtResponseDTO loginAdmin(LoginDTO dto) {
        Admin admin = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
        if (!passwordEncoder.matches(dto.getPassword(), admin.getPasswordHash())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        String token = jwtUtils.generateToken(admin.getId(), admin.getEmail(), "ADMIN");
        return JwtResponseDTO.builder()
                .token(token)
                .userId(admin.getId())
                .email(admin.getEmail())
                .name(admin.getName())
                .role("ADMIN")
                .build();
    }
}