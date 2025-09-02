package com.example.lms.service;

import com.example.lms.dto.course.CourseCreateRequest;
import com.example.lms.dto.course.CourseDto;
import com.example.lms.dto.course.CourseMapper;
import com.example.lms.dto.course.CourseUpdateRequest;
import com.example.lms.entity.Course;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(CourseMapper::toDto)
                .collect(Collectors.toList());
    }

    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return CourseMapper.toDto(course);
    }

    public CourseDto createCourse(CourseCreateRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Course with code " + request.getCode() + " already exists.");
        }
        Course course = CourseMapper.toEntity(request);
        return CourseMapper.toDto(courseRepository.save(course));
    }

    public CourseDto updateCourse(Long id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        CourseMapper.updateEntityFromDto(request, course);
        return CourseMapper.toDto(courseRepository.save(course));
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }
}