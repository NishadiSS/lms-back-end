package com.example.lms.dto.enrollment;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnrollRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;
    @NotNull(message = "Course ID is required")
    private Long courseId;
}