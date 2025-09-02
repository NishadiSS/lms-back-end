package com.example.lms.controller;

import com.example.lms.dto.instructor.InstructorCreateRequest;
import com.example.lms.dto.instructor.InstructorDto;
import com.example.lms.dto.instructor.InstructorUpdateRequest;
import com.example.lms.service.InstructorService; // InstructorService එක ඔබ විසින් නිර්මාණය කළ යුතුය
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;

    // සියලුම instructors ලබා ගැනීම (Admin, Instructor, Student ට)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<List<InstructorDto>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }

    // තනි instructor කෙනෙක් ලබා ගැනීම (Admin, Instructor, Student ට)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR', 'STUDENT')")
    public ResponseEntity<InstructorDto> getInstructorById(@PathVariable Long id) {
        return ResponseEntity.ok(instructorService.getInstructorById(id));
    }

    // නව instructor කෙනෙක් ලියාපදිංචි කිරීම (Admin ට පමණක්)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InstructorDto> createInstructor(@Valid @RequestBody InstructorCreateRequest request) {
        return new ResponseEntity<>(instructorService.createInstructor(request), HttpStatus.CREATED);
    }

    // Instructor කෙනෙකුගේ profile එක update කිරීම (Admin, හෝ තමන්ගේ profile එක නම් Instructor ට)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN') or (hasRole('INSTRUCTOR') and @securityUtils.isInstructorOwner(#id))")
    public ResponseEntity<InstructorDto> updateInstructor(@PathVariable Long id, @Valid @RequestBody InstructorUpdateRequest request) {
        return ResponseEntity.ok(instructorService.updateInstructor(id, request));
    }

    // Instructor කෙනෙක් delete කිරීම (Admin ට පමණක්)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructor(id);
        return ResponseEntity.noContent().build();
    }
}