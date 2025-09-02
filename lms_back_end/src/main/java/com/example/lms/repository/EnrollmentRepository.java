package com.example.lms.repository;

import com.example.lms.entity.Course;
import com.example.lms.entity.Enrollment;
import com.example.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    Boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}