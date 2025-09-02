package com.example.lms.service;

import com.example.lms.dto.enrollment.EnrollRequest;
import com.example.lms.dto.enrollment.EnrollmentDto;
import com.example.lms.dto.enrollment.EnrollmentMapper;
import com.example.lms.entity.Course;
import com.example.lms.entity.Enrollment;
import com.example.lms.entity.Student;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public List<EnrollmentDto> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(EnrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public EnrollmentDto getEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found with id: " + id));
        return EnrollmentMapper.toDto(enrollment);
    }

    public List<EnrollmentDto> getEnrollmentsByStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new ResourceNotFoundException("Student not found with id: " + studentId);
        }
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(EnrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EnrollmentDto> getEnrollmentsByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course not found with id: " + courseId);
        }
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(EnrollmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentDto enrollStudentInCourse(EnrollRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + request.getCourseId()));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new IllegalArgumentException("Student " + student.getStudentId() + " is already enrolled in course " + course.getCode());
        }

        Enrollment enrollment = EnrollmentMapper.toEntity(student, course);
        return EnrollmentMapper.toDto(enrollmentRepository.save(enrollment));
    }

    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Enrollment not found with id: " + id);
        }
        enrollmentRepository.deleteById(id);
    }
}