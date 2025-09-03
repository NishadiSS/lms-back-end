package com.example.lms.controller;

import com.example.lms.dto.student.StudentCreateRequest;
import com.example.lms.dto.student.StudentDto;
import com.example.lms.dto.student.StudentProfileUpdateRequest;
import com.example.lms.service.StudentService; 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

   
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR','STUDENT')")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') or (hasRole('STUDENT') and @securityUtils.isStudentOwner(#id))")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

   
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        return new ResponseEntity<>(studentService.createStudent(request), HttpStatus.CREATED);
    }

   
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') or (hasRole('STUDENT') and @securityUtils.isStudentOwner(#id))")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentProfileUpdateRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}