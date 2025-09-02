package com.example.lms.service;

import com.example.lms.dto.grade.GradeDto;
import com.example.lms.dto.grade.GradeMapper;
import com.example.lms.dto.grade.GradeUpsertRequest;
import com.example.lms.entity.Enrollment;
import com.example.lms.entity.Grade;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.GradeRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository; // For existence checks if needed
    private final CourseRepository courseRepository;   // For existence checks if needed

    public List<GradeDto> getAllGrades() {
        return gradeRepository.findAll().stream()
                .map(GradeMapper::toDto)
                .collect(Collectors.toList());
    }

    public GradeDto getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Grade not found with id: " + id));
        return GradeMapper.toDto(grade);
    }

    public List<GradeDto> getGradesByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return gradeRepository.findByEnrollment_Student_Id(studentId).stream()
                .map(GradeMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<GradeDto> getGradesByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        return gradeRepository.findByEnrollment_Course_Id(courseId).stream()
                .map(GradeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public GradeDto upsertGrade(GradeUpsertRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + request.getEnrollmentId()));

        Optional<Grade> existingGrade = gradeRepository.findByEnrollment(enrollment);

        Grade grade;
        if (existingGrade.isPresent()) {
            // Update existing grade
            grade = existingGrade.get();
            GradeMapper.updateEntityFromDto(request, grade);
        } else {
            // Create new grade
            grade = GradeMapper.toEntity(request, enrollment);
        }
        return GradeMapper.toDto(gradeRepository.save(grade));
    }

    public void deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Grade not found with id: " + id);
        }
        gradeRepository.deleteById(id);
    }
}