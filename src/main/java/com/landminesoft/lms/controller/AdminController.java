package com.landminesoft.lms.controller;

import com.landminesoft.lms.entity.*;
import com.landminesoft.lms.repository.*;
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
public class AdminController {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    // Only ADMIN role can access this
    @GetMapping("/students")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/faculty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FacultyPersonal>> getAllFaculty() {
        return ResponseEntity.ok(facultyRepository.findAll());
    }

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