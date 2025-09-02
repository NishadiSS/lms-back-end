package com.example.lms.dto.student;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class StudentCreateRequest {
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Student ID is required")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Student ID must be alphanumeric")
    private String studentId; // e.g., S001
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private Long userId; // Optional: For linking with an existing User account
}