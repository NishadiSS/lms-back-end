package com.example.lms.dto.grade;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.Data;

@Data
public class GradeUpsertRequest {
    @NotNull(message = "Enrollment ID is required")
    private Long enrollmentId;
    @NotBlank(message = "Grade value is required")
    private String gradeValue;
    @NotNull(message = "Score is required")
    @DecimalMin(value = "0.0", message = "Score must be at least 0.0")
    @DecimalMax(value = "100.0", message = "Score must be at most 100.0")
    private Double score;
    private String remarks;
}