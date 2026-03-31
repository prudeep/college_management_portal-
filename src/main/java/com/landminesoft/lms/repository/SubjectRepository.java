package com.landminesoft.lms.repository;

import com.landminesoft.lms.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Boolean existsBySubjectCode(String subjectCode);
}