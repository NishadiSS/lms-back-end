package com.example.lms.dto.enrollment;

import com.example.lms.dto.course.CourseMapper;
import com.example.lms.dto.student.StudentMapper;
import com.example.lms.entity.Course;
import com.example.lms.entity.Enrollment;
import com.example.lms.entity.Student;

public class EnrollmentMapper {

    public static EnrollmentDto toDto(Enrollment enrollment) {
        if (enrollment == null) return null;
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());
        dto.setStudent(StudentMapper.toDto(enrollment.getStudent()));
        dto.setCourse(CourseMapper.toDto(enrollment.getCourse()));
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        return dto;
    }

    public static Enrollment toEntity(Student student, Course course) {
        if (student == null || course == null) return null;
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        // enrollmentDate is set automatically by default in entity
        return enrollment;
    }
}