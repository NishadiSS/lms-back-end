package com.example.lms.service;

import com.example.lms.dto.instructor.InstructorCreateRequest;
import com.example.lms.dto.instructor.InstructorDto;
import com.example.lms.dto.instructor.InstructorMapper; 
import com.example.lms.dto.instructor.InstructorUpdateRequest;
import com.example.lms.entity.ERole;
import com.example.lms.entity.Instructor;
import com.example.lms.entity.Role;
import com.example.lms.entity.User;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.InstructorRepository;
import com.example.lms.repository.RoleRepository;
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
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<InstructorDto> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(InstructorMapper::toDto) 
                .collect(Collectors.toList());
    }

    public InstructorDto getInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));
        return InstructorMapper.toDto(instructor); 
    }

    @Transactional
    public InstructorDto createInstructor(InstructorCreateRequest request) {
        if (instructorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Instructor with email " + request.getEmail() + " already exists.");
        }

        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            if (instructorRepository.existsByUserId(request.getUserId())) {
                throw new IllegalArgumentException("User with id " + request.getUserId() + " is already linked to an instructor.");
            }
        } else {
            // New user account will be created if not provided
            String defaultUsername = request.getFirstName() + "." + request.getLastName();
            if (userRepository.existsByUsername(defaultUsername)) {
                throw new IllegalArgumentException("Default username " + defaultUsername + " is already taken for a new user.");
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email " + request.getEmail() + " is already in use for a new user.");
            }
            user = new User(defaultUsername, request.getEmail(), passwordEncoder.encode("instructor123"));
            Set<Role> roles = new HashSet<>();
            Role instructorRole = roleRepository.findByName(ERole.ROLE_INSTRUCTOR)
                    .orElseThrow(() -> new ResourceNotFoundException("Error: Instructor Role is not found."));
            roles.add(instructorRole);
            user.setRoles(roles);
            user = userRepository.save(user);
        }

        Instructor instructor = InstructorMapper.toEntity(request, user);
        return InstructorMapper.toDto(instructorRepository.save(instructor));
    }

    public InstructorDto updateInstructor(Long id, InstructorUpdateRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));

        if (!instructor.getEmail().equals(request.getEmail()) && instructorRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email " + request.getEmail() + " is already in use by another instructor.");
        }

        InstructorMapper.updateEntityFromDto(request, instructor);
        return InstructorMapper.toDto(instructorRepository.save(instructor));
    }

    public void deleteInstructor(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Instructor not found with id: " + id));
        if (instructor.getUser() != null) {
            userRepository.delete(instructor.getUser());
        }
        instructorRepository.delete(instructor);
    }
}