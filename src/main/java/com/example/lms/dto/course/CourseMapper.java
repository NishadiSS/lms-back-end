package com.example.lms.dto.course;
import com.example.lms.entity.Course;
public class CourseMapper {
    public static CourseDto toDto(Course course) {
        if (course == null) return null;
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        return dto;
    }
    public static Course toEntity(CourseCreateRequest request) {
        if (request == null) return null;
        Course course = new Course();
        course.setCode(request.getCode());
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        return course;
    }
    public static void updateEntityFromDto(CourseUpdateRequest request, Course course) {
        if (request == null || course == null) return;
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
    }
}