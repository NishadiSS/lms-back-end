package com.example.lms.config;

import com.example.lms.entity.User; 
import com.example.lms.entity.ERole;
import com.example.lms.repository.UserRepository; 
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

@Component("securityUtils") 
@RequiredArgsConstructor 
public class SecurityUtils {

    private final CustomUserDetailsService userDetailsService; 
    private final UserRepository userRepository; 
    private final StudentRepository studentRepository; 
    private final InstructorRepository instructorRepository; 
    private final EnrollmentRepository enrollmentRepository; 
    private final GradeRepository gradeRepository; 

    
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
           
            return userRepository.findByUsername(username)
                    .map(User::getId)
                    .orElse(null);
        }
        return null;
    }


    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_ADMIN.name()));
    }


    public boolean isInstructor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_INSTRUCTOR.name()));
    }


    public boolean isStudent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(ERole.ROLE_STUDENT.name()));
    }


    public boolean isStudentOwner(Long studentId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return studentRepository.findById(studentId)
                .map(student -> Objects.equals(student.getUser().getId(), currentUserId))
                .orElse(false);
    }

   
    public boolean isInstructorOwner(Long instructorId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return instructorRepository.findById(instructorId)
                .map(instructor -> Objects.equals(instructor.getUser().getId(), currentUserId))
                .orElse(false);
    }

  
    public boolean isEnrollmentOwner(Long enrollmentId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return enrollmentRepository.findById(enrollmentId)
                .map(enrollment -> Objects.equals(enrollment.getStudent().getUser().getId(), currentUserId))
                .orElse(false);
    }

    
    public boolean isGradeOwner(Long gradeId) {
        if (isAdmin()) return true;

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return gradeRepository.findById(gradeId)
                .map(grade -> Objects.equals(grade.getEnrollment().getStudent().getUser().getId(), currentUserId))
                .orElse(false);
    }

    
    public boolean isEnrollmentOwnerOrAdminOrInstructor(Long enrollmentId) {
        if (isAdmin()) return true; 

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return enrollmentRepository.findById(enrollmentId).map(enrollment -> {
        
            if (Objects.equals(enrollment.getStudent().getUser().getId(), currentUserId)) return true;

            
            return isInstructor();
        }).orElse(false);
    }

   
    public boolean isGradeOwnerOrAdminOrInstructor(Long gradeId) {
        if (isAdmin()) return true; 

        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) return false;

        return gradeRepository.findById(gradeId)
                .map(grade -> Objects.equals(grade.getEnrollment().getStudent().getUser().getId(), currentUserId))
                .orElse(false);
    }
}