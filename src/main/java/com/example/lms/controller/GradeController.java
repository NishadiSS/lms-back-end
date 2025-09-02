package com.example.lms.controller;

import com.example.lms.dto.grade.GradeDto;
import com.example.lms.dto.grade.GradeUpsertRequest;
import com.example.lms.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    // සියලුම grades ලබා ගැනීම (Admin ට පමණක්)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')") // Student ට සියලුම grades දැකීමට ඉඩ නොදිය යුතුය
    public ResponseEntity<List<GradeDto>> getAllGrades() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    // තනි grade එකක් ලබා ගැනීම (Admin, Instructor, හෝ අදාළ Student ට)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') or (hasRole('STUDENT') and @securityUtils.isGradeOwner(#id))") // මෙහිදී Student ට තමන්ගේම grade බැලීමට අවසර තිබිය යුතුය
    public ResponseEntity<GradeDto> getGradeById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.getGradeById(id));
    }

    // Student කෙනෙකුගේ සියලුම grades ලබා ගැනීම (Admin, Instructor, හෝ අදාළ Student ට)
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR') or (hasRole('STUDENT') and @securityUtils.isStudentOwner(#studentId))") // මෙහිදී Student ට තමන්ගේම grades බැලීමට අවසර තිබිය යුතුය
    public ResponseEntity<List<GradeDto>> getGradesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
    }

    // Course එකක සියලුම grades ලබා ගැනීම (Admin, Instructor ට පමණක්)
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<List<GradeDto>> getGradesByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(gradeService.getGradesByCourse(courseId));
    }

    // Grade එකක් නිර්මාණය කිරීම/යාවත්කාලීන කිරීම (Admin, Instructor ට පමණක්)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<GradeDto> upsertGrade(@Valid @RequestBody GradeUpsertRequest request) {
        return new ResponseEntity<>(gradeService.upsertGrade(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // Grade update කිරීම සඳහා
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<GradeDto> updateGrade(@PathVariable Long id, @Valid @RequestBody GradeUpsertRequest request) {
        return ResponseEntity.ok(gradeService.upsertGrade(request)); // upsertGrade එක update logic එක handle කරයි
    }


    // Grade එකක් delete කිරීම (Admin ට පමණක්)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}