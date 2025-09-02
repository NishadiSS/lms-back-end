package com.example.lms.controller;

import com.example.lms.dto.enrollment.EnrollRequest;
import com.example.lms.dto.enrollment.EnrollmentDto;
import com.example.lms.dto.student.StudentCreateRequest;
import com.example.lms.dto.student.StudentDto;
import com.example.lms.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')") 
    public ResponseEntity<List<EnrollmentDto>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }

   
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') or (hasRole('STUDENT') and @securityUtils.isEnrollmentOwner(#id))")
    public ResponseEntity<EnrollmentDto> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(id));
    }

    // Student කෙනෙකුගේ සියලුම enrollments ලබා ගැනීම (Admin, Instructor, හෝ අදාළ Student ට)
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') or (hasRole('STUDENT') and @securityUtils.isStudentOwner(#studentId))") // මෙහිදී Student ට තමන්ගේම enrollments බැලීමට අවසර තිබිය යුතුය
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    // Course එකක සියලුම enrollments ලබා ගැනීම (Admin, Instructor ට පමණක්)
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

    // Course එකකට student කෙනෙක් enroll කිරීම (Admin, හෝ අදාළ Student ට)
   @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR')")
    public ResponseEntity<EnrollmentDto> enrollStudentInCourse(@Valid @RequestBody EnrollRequest request) {
        return new ResponseEntity<>(enrollmentService.enrollStudentInCourse(request), HttpStatus.CREATED);
    }

    


    // Enrollment එකක් delete කිරීම / student කෙනෙක් course එකකින් withdraw කිරීම (Admin ට පමණක්)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    //@PostMapping("/self-enroll")
    //@PreAuthorize("hasRole('STUDENT')")
    //public ResponseEntity<EnrollmentDto> selfEnroll(@Valid @RequestBody EnrollRequest request) {
       // return new ResponseEntity<>(enrollmentService.enrollStudentInCourse(request), HttpStatus.CREATED);
    //}
}