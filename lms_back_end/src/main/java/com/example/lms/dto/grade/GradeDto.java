package com.example.lms.dto.grade;

import com.example.lms.dto.enrollment.EnrollmentDto;
import lombok.Data;

@Data
public class GradeDto {
    private Long id;
    private EnrollmentDto enrollment;
    private String gradeValue;
    private Double score;
    private String remarks;
}