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

  
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
    //@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') or (hasRole('STUDENT') and @securityUtils.isStudentOwner(#studentId))") // මෙහිදී Student ට තමන්ගේම enrollments බැලීමට අවසර තිබිය යුතුය
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<List<EnrollmentDto>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }

   
   @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','INSTRUCTOR','STUDENT')")
    public ResponseEntity<EnrollmentDto> enrollStudentInCourse(@Valid @RequestBody EnrollRequest request) {
        return new ResponseEntity<>(enrollmentService.enrollStudentInCourse(request), HttpStatus.CREATED);
    }

    


    
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