package com.example.lms.dto.student;

import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String studentId;
    private String email;
    private Long userId; // Linked User ID
}