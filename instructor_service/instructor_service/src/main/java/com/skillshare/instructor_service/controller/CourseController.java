package com.skillshare.instructor_service.controller;

import com.skillshare.instructor_service.dto.CourseDTO;
import com.skillshare.instructor_service.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping("/addcourse")
    public ResponseEntity<CourseDTO> addCourse(@Valid @RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        return ResponseEntity.ok(createdCourse);
    }

    @GetMapping("/getcourses")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long courseId) {
        CourseDTO course = courseService.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/updatecourse/{courseId}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long courseId, @Valid @RequestBody CourseDTO courseDTO) {
        if (courseId <= 0) {
            throw new IllegalArgumentException("Invalid course ID: " + courseId);
        }
        courseDTO.setId(courseId); // Ensure the ID from the path is used
        CourseDTO updatedCourse = courseService.updateCourse(courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/deletecourse/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        courseService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }
}