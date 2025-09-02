package com.example.lms.dto.grade;

import com.example.lms.dto.enrollment.EnrollmentMapper;
import com.example.lms.entity.Enrollment;
import com.example.lms.entity.Grade;

public class GradeMapper {

    public static GradeDto toDto(Grade grade) {
        if (grade == null) return null;
        GradeDto dto = new GradeDto();
        dto.setId(grade.getId());
        dto.setEnrollment(EnrollmentMapper.toDto(grade.getEnrollment()));
        dto.setGradeValue(grade.getGradeValue());
        dto.setScore(grade.getScore());
        dto.setRemarks(grade.getRemarks());
        return dto;
    }

    public static Grade toEntity(GradeUpsertRequest request, Enrollment enrollment) {
        if (request == null || enrollment == null) return null;
        Grade grade = new Grade();
        grade.setEnrollment(enrollment);
        grade.setGradeValue(request.getGradeValue());
        grade.setScore(request.getScore());
        grade.setRemarks(request.getRemarks());
        return grade;
    }

    public static void updateEntityFromDto(GradeUpsertRequest request, Grade grade) {
        if (request == null || grade == null) return;
        grade.setGradeValue(request.getGradeValue());
        grade.setScore(request.getScore());
        grade.setRemarks(request.getRemarks());
    }
}