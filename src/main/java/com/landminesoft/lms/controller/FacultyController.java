package com.landminesoft.lms.controller;

import com.landminesoft.lms.entity.Student;
import com.landminesoft.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FacultyController {

    private final StudentRepository studentRepository;

    // Only FACULTY role can access this
    @GetMapping("/students")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }
}