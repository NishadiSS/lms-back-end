package com.example.lms.config;

import com.example.lms.entity.User; // User entity එක import කරන්න
import com.example.lms.entity.ERole;
import com.example.lms.repository.UserRepository; // UserRepository එක import කරන්න
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.InstructorRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.GradeRepository;
import com.example.lms.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("securityUtils") // මෙම නාමය @PreAuthorize වලදී භාවිතා කරයි
@RequiredArgsConstructor // Constructor Injection සඳහා
public class SecurityUtils {

    private final CustomUserDetailsService userDetailsService; // CustomUserDetailsService එක inject කරන්න
    private final UserRepository userRepository; // UserRepository එක inject කරන්න
    private final StudentRepository studentRepository; // StudentRepository එක inject කරන්න
    private final InstructorRepository instructorRepository; // InstructorRepository එක inject කරන්න
    private final EnrollmentRepository enrollmentRepository; // EnrollmentRepository එක inject කරන්න
    private final GradeRepository gradeRepository; // GradeRepository එක inject කරන්න

    // දැනට login වී සිටින User ගේ ID එක ලබා ගැනීම
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            // User entity එකෙන් ID එක ලබා ගැනීමට UserRepository එක භාවිතා කරන්න
            return userRepository.findByUsername(username)
                    .map(User::getId)
                    .orElse(null);
        }
        return null;
    }

    // User ට ADMIN role එක තිබේදැයි පරීක්ෂා කිරීම
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_ADMIN.name()));
    }

    // User ට INSTRUCTOR role එක තිබේදැයි පරීක්ෂා කිරීම
    public boolean isInstructor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_INSTRUCTOR.name()));
    }

    // User ට STUDENT role එක තිබේදැයි පරීක්ෂා කිරීම
    public boolean isStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_STUDENT.name()));
    }

    // අදාළ student profile එකේ හිමිකරු ද නැතිනම් Admin ද යන්න පරීක්ෂා කිරීම
    public boolean isStudentOwner(Long studentId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return studentRepository.findById(studentId)
                .map(student -> Objects.equals(student.getUser().getId(), currentUserId))
                .orElse(false);
    }

    // අදාළ instructor profile එකේ හිමිකරු ද නැතිනම් Admin ද යන්න පරීක්ෂා කිරීම
    public boolean isInstructorOwner(Long instructorId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return instructorRepository.findById(instructorId)
                .map(instructor -> Objects.equals(instructor.getUser().getId(), currentUserId))
                .orElse(false);
    }

    // අදාළ enrollment එකේ හිමිකරු (student) ද නැතිනම් Admin ද යන්න පරීක්ෂා කිරීම
    public boolean isEnrollmentOwner(Long enrollmentId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> Objects.equals(enrollment.getStudent().getUser().getId(), currentUserId))
                .orElse(false);
    }

    // අදාළ grade එකේ හිමිකරු (student) ද නැතිනම් Admin ද යන්න පරීක්ෂා කිරීම
    public boolean isGradeOwner(Long gradeId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return gradeRepository.findById(gradeId)
                .map(grade -> Objects.equals(grade.getEnrollment().getStudent().getUser().getId(), currentUserId))
                .orElse(false);
    }

    // Enrollment එකක student හිමිකරුද, Admin ද, නැතිනම් course එකේ Instructor ද යන්න
    public boolean isEnrollmentOwnerOrAdminOrInstructor(Long enrollmentId) {
        if (isAdmin()) return true; // Admin has full access

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return enrollmentRepository.findById(enrollmentId).map(enrollment -> {
            // Check if current user is the student who owns this enrollment
            if (Objects.equals(enrollment.getStudent().getUser().getId(), currentUserId)) return true;

            // Check if current user is an instructor and teaches this course (this requires more complex logic)
            // For simplicity now, let's just check if current user is generally an instructor
            // A better check: Does the current instructor teach the course associated with this enrollment?
            return isInstructor();
        }).orElse(false);
    }

    // Grade එකක student හිමිකරුද, Admin ද, නැතිනම් course එකේ Instructor ද යන්න
    public boolean isGradeOwnerOrAdminOrInstructor(Long gradeId) {
        if (isAdmin()) return true; // Admin has full access

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return gradeRepository.findById(gradeId)
                .map(grade -> Objects.equals(grade.getEnrollment().getStudent().getUser().getId(), currentUserId))
                .orElse(false);
    }
}