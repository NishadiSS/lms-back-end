package com.example.lms.dto.instructor;

import lombok.Data;

@Data
public class InstructorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long userId; // Linked User ID
}