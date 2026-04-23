package com.landminesoft.lms.controller;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.repository.EnrollmentRepository;
import com.landminesoft.lms.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final ProfileService profileService;
    private final EnrollmentRepository enrollmentRepository;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.getStudentProfile(email));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileDTO dto) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.updateStudentProfile(email, dto));
    }

    @GetMapping("/enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getEnrollments(Authentication authentication) {
        Student student = profileService.getStudentProfile(authentication.getName());
        return ResponseEntity.ok(enrollmentRepository.findByStudentId(student.getId()));
    }
}