package com.landminesoft.lms.controller;

import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.repository.StudentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Faculty", description = "Faculty management APIs - FACULTY role required")
@SecurityRequirement(name = "Bearer Authentication")
public class FacultyController {

    private final StudentRepository studentRepository;

    @Operation(summary = "Get all students - Faculty only")
    @GetMapping("/students")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }
}