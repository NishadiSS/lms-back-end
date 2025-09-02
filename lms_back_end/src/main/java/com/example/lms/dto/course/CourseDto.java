package com.example.lms.dto.course;
import lombok.Data;
@Data
public class CourseDto {
    private Long id;
    private String code;
    private String title;
    private String description;
}