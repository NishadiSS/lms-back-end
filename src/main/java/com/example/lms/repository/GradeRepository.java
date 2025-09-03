package com.example.lms.repository;

import com.example.lms.entity.Enrollment;
import com.example.lms.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByEnrollment(Enrollment enrollment);
    Optional<Grade> findByEnrollmentId(Long enrollmentId); 
    List<Grade> findByEnrollment_Student_Id(Long studentId);
    List<Grade> findByEnrollment_Course_Id(Long courseId);
}