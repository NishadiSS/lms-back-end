package com.example.lms.dto.instructor;

import com.example.lms.entity.Instructor;
import com.example.lms.entity.User;

public class InstructorMapper {

    public static InstructorDto toDto(Instructor instructor) {
        if (instructor == null) return null;
        InstructorDto dto = new InstructorDto();
        dto.setId(instructor.getId());
        dto.setFirstName(instructor.getFirstName());
        dto.setLastName(instructor.getLastName());
        dto.setEmail(instructor.getEmail());
        if (instructor.getUser() != null) {
            dto.setUserId(instructor.getUser().getId());
        }
        return dto;
    }

    public static Instructor toEntity(InstructorCreateRequest request, User user) {
        if (request == null) return null;
        Instructor instructor = new Instructor();
        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setEmail(request.getEmail());
        instructor.setUser(user);
        return instructor;
    }

    public static void updateEntityFromDto(InstructorUpdateRequest request, Instructor instructor) {
        if (request == null || instructor == null) return;
        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setEmail(request.getEmail());
    }
}