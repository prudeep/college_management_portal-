package com.landminesoft.lms.controller;

import com.landminesoft.lms.dto.UpdateProfileDTO;
import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.repository.EnrollmentRepository;
import com.landminesoft.lms.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Student", description = "Student profile and enrollment APIs - STUDENT role required")
@SecurityRequirement(name = "Bearer Authentication")
public class StudentController {

    private final ProfileService profileService;
    private final EnrollmentRepository enrollmentRepository;

    @Operation(summary = "Get student profile")
    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> getProfile(Authentication authentication) {
        return ResponseEntity.ok(profileService.getStudentProfile(
                authentication.getName()));
    }

    @Operation(summary = "Update student profile")
    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileDTO dto) {
        return ResponseEntity.ok(profileService.updateStudentProfile(
                authentication.getName(), dto));
    }

    @Operation(summary = "Get student enrollments")
    @GetMapping("/enrollments")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getEnrollments(Authentication authentication) {
        Student student = profileService.getStudentProfile(authentication.getName());
        return ResponseEntity.ok(enrollmentRepository.findByStudentId(student.getId()));
    }
}