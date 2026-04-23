package com.landminesoft.lms.service;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.entity.*;
import com.landminesoft.lms.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Get student profile
    public Student getStudentProfile(String email) {
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Update student profile
    public Student updateStudentProfile(String email, UpdateProfileDTO dto) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (dto.getPhone() != null) student.setPhone(dto.getPhone());
        if (dto.getAddress() != null) student.setAddress(dto.getAddress());
        if (dto.getCity() != null) student.setCity(dto.getCity());
        if (dto.getPincode() != null) student.setPincode(dto.getPincode());
        if (dto.getDob() != null) student.setDob(dto.getDob());

        return studentRepository.save(student);
    }

    // Change password
    public String changePassword(String email, ChangePasswordDTO dto) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), student.getPasswordHash())) {
            throw new RuntimeException("Old password is incorrect");
        }

        student.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        studentRepository.save(student);
        return "Password changed successfully";
    }
}