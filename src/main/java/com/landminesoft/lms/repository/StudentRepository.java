package com.landminesoft.lms.repository;

import com.landminesoft.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByRollNumber(String rollNumber);
}