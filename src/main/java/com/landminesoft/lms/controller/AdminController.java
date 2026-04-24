package com.landminesoft.lms.controller;

import com.landminesoft.lms.entity.*;
import com.landminesoft.lms.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin", description = "Admin management APIs - ADMIN role required")
@SecurityRequirement(name = "Bearer Authentication")
public class AdminController {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Operation(summary = "Get all students - Admin only")
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @Operation(summary = "Get all faculty - Admin only")
    @GetMapping("/faculty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FacultyPersonal>> getAllFaculty() {
        return ResponseEntity.ok(facultyRepository.findAll());
    }

    @Operation(summary = "Get system reports - Admin only")
    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getReports() {
        Map<String, Object> report = new HashMap<>();
        report.put("totalStudents", studentRepository.count());
        report.put("totalFaculty", facultyRepository.count());
        report.put("generatedAt", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(report);
    }
}