package com.example.lms.dto.course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class CourseUpdateRequest {
    @NotBlank @Size(min = 3, max = 100) private String title;
    private String description;
}