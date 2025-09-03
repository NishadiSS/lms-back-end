package com.example.lms.repository;

import com.example.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByUserId(Long userId); 
    Boolean existsByStudentId(String studentId);
    Boolean existsByEmail(String email);
    Boolean existsByUserId(Long userId);
}