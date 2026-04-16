package com.landminesoft.lms.service;

import com.landminesoft.lms.dto.RegisterResponseDTO;
import com.landminesoft.lms.dto.StudentRegisterDTO;
import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RegisterResponseDTO registerStudent(StudentRegisterDTO dto) {

        // Check duplicate email
        if (studentRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Auto-generate roll number: e.g. CSE2024001
        String rollNumber = generateRollNumber(dto.getBranch(), dto.getEnrollmentYear());

        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        student.setBranch(dto.getBranch());
        student.setEnrollmentYear(dto.getEnrollmentYear());
        student.setRollNumber(rollNumber);

        studentRepository.save(student);

        return new RegisterResponseDTO(
            student.getId(),
            student.getName(),
            student.getEmail(),
            "STUDENT",
            "Registration successful. Please login."
        );
    }

    private String generateRollNumber(String branch, Integer enrollmentYear) {
        long count = studentRepository.countByBranchAndEnrollmentYear(branch, enrollmentYear);
        return branch.toUpperCase() + enrollmentYear + String.format("%03d", count + 1);
        // Result: CSE2024001, CSE2024002, etc.
    }
}