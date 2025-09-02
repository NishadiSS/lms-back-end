package com.example.lms.dto.student;

import com.example.lms.entity.Student;
import com.example.lms.entity.User;

public class StudentMapper {

    public static StudentDto toDto(Student student) {
        if (student == null) return null;
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setStudentId(student.getStudentId());
        dto.setEmail(student.getEmail());
        if (student.getUser() != null) {
            dto.setUserId(student.getUser().getId());
        }
        return dto;
    }

    public static Student toEntity(StudentCreateRequest request, User user) {
        if (request == null) return null;
        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setStudentId(request.getStudentId());
        student.setEmail(request.getEmail());
        student.setUser(user);
        return student;
    }

    public static void updateEntityFromDto(StudentProfileUpdateRequest request, Student student) {
        if (request == null || student == null) return;
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
    }
}