package com.example.lms.repository;

import com.example.lms.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Optional<Instructor> findByEmail(String email);
    Optional<Instructor> findByUserId(Long userId); 
    Boolean existsByEmail(String email);
    Boolean existsByUserId(Long userId);
}