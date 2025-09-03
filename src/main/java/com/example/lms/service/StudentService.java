package com.example.lms.service;

import com.example.lms.dto.student.StudentCreateRequest;
import com.example.lms.dto.student.StudentDto;
import com.example.lms.dto.student.StudentMapper;
import com.example.lms.dto.student.StudentProfileUpdateRequest;
import com.example.lms.entity.ERole;
import com.example.lms.entity.Role;
import com.example.lms.entity.Student;
import com.example.lms.entity.User;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.RoleRepository;
import com.example.lms.repository.StudentRepository;
import com.example.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; 

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(StudentMapper::toDto)
                .collect(Collectors.toList());
    }

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return StudentMapper.toDto(student);
    }

    @Transactional
    public StudentDto createStudent(StudentCreateRequest request) {
        if (studentRepository.existsByStudentId(request.getStudentId())) {
            throw new IllegalArgumentException("Student with ID " + request.getStudentId() + " already exists.");
        }
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Student with email " + request.getEmail() + " already exists.");
        }

        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            if (studentRepository.existsByUserId(request.getUserId())) {
                throw new IllegalArgumentException("User with id " + request.getUserId() + " is already linked to a student.");
            }
        } else {
            // New user account will be created if not provided
            String defaultUsername = request.getStudentId(); // Or generate a better one
            if (userRepository.existsByUsername(defaultUsername)) {
                throw new IllegalArgumentException("Default username " + defaultUsername + " is already taken for a new user.");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email " + request.getEmail() + " is already in use for a new user.");
            }
            user = new User(defaultUsername, request.getEmail(), passwordEncoder.encode(request.getStudentId())); // Use student ID as initial password, or generate
            Set<Role> roles = new HashSet<>();
            Role studentRole = roleRepository.findByName(ERole.ROLE_STUDENT)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Student Role is not found."));
            roles.add(studentRole);
            user.setRoles(roles);
            user = userRepository.save(user);
        }

        Student student = StudentMapper.toEntity(request, user);
        return StudentMapper.toDto(studentRepository.save(student));
    }

    public StudentDto updateStudent(Long id, StudentProfileUpdateRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // Check if email is being changed and if new email already exists (for another student)
        if (!student.getEmail().equals(request.getEmail()) && studentRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email " + request.getEmail() + " is already in use by another student.");
        }

        StudentMapper.updateEntityFromDto(request, student);
        return StudentMapper.toDto(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        // Also delete the linked user account if it was created specifically for this student
        // This is optional and depends on business logic, be careful not to delete users with other roles.
        if (student.getUser() != null) {
            userRepository.delete(student.getUser());
        }
        studentRepository.delete(student);
    }
}