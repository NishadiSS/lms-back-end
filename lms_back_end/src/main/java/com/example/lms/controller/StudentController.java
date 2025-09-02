package com.example.lms.controller;

import com.example.lms.dto.student.StudentCreateRequest;
import com.example.lms.dto.student.StudentDto;
import com.example.lms.dto.student.StudentProfileUpdateRequest;
import com.example.lms.service.StudentService; // StudentService එක ඔබ විසින් නිර්මාණය කළ යුතුය
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

    // සියලුම students ලබා ගැනීම (Admin, Instructor ට පමණක්)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR','STUDENT')")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    // තනි student කෙනෙක් ලබා ගැනීම (Admin, Instructor, හෝ තමන්ගේ profile එක නම් Student ට)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') or (hasRole('STUDENT') and @securityUtils.isStudentOwner(#id))")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    // නව student කෙනෙක් ලියාපදිංචි කිරීම (Admin ට පමණක්)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'INSTRUCTOR')")
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        return new ResponseEntity<>(studentService.createStudent(request), HttpStatus.CREATED);
    }

    // Student කෙනෙකුගේ profile එක update කිරීම (Admin, හෝ තමන්ගේ profile එක නම් Student ට)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') or (hasRole('STUDENT') and @securityUtils.isStudentOwner(#id))")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentProfileUpdateRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }

    // Student කෙනෙක් delete කිරීම (Admin ට පමණක්)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}