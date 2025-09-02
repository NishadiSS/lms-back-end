package com.example.lms.dto.enrollment;

import com.example.lms.dto.course.CourseDto;
import com.example.lms.dto.student.StudentDto;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrollmentDto {
    private Long id;
    private StudentDto student;
    private CourseDto course;
    private LocalDateTime enrollmentDate;
}