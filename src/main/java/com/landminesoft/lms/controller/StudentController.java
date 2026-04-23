package com.landminesoft.lms.controller;

import com.landminesoft.lms.dto.*;
import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {

    private final ProfileService profileService;

    // GET /api/student/profile
    @GetMapping("/profile")
    public ResponseEntity<Student> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.getStudentProfile(email));
    }

    // PUT /api/student/profile
    @PutMapping("/profile")
    public ResponseEntity<Student> updateProfile(
            Authentication authentication,
            @RequestBody UpdateProfileDTO dto) {
        String email = authentication.getName();
        return ResponseEntity.ok(profileService.updateStudentProfile(email, dto));
    }
}