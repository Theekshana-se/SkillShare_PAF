package com.skillshare.instructor_service.service;

import com.skillshare.instructor_service.dto.CourseDTO;
import com.skillshare.instructor_service.dto.LessonDTO;
import com.skillshare.instructor_service.dto.ModuleDTO;
import com.skillshare.instructor_service.ResourceNotFoundException;
import com.skillshare.instructor_service.model.Course;
import com.skillshare.instructor_service.model.Lesson;
import com.skillshare.instructor_service.model.Module;
import com.skillshare.instructor_service.repo.CourseRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CourseDTO createCourse(CourseDTO courseDTO) {
        logger.info("Creating course: {}", courseDTO.getTitle());
        Course course = new Course();
        course.setTitle(courseDTO.getTitle());
        course.setShortDescription(courseDTO.getShortDescription());
        course.setFullDescription(courseDTO.getFullDescription());
        course.setLevel(courseDTO.getLevel());
        course.setThumbnail(courseDTO.getThumbnail());
        course.setTags(courseDTO.getTags());

        List<Module> modules = courseDTO.getModules().stream().map(moduleDTO -> {
            Module module = new Module();
            module.setModuleTitle(moduleDTO.getModuleTitle());
            module.setCourse(course);
            List<Lesson> lessons = moduleDTO.getLessons().stream().map(lessonDTO -> {
                Lesson lesson = new Lesson();
                lesson.setLessonTitle(lessonDTO.getLessonTitle());
                lesson.setVideoUrl(lessonDTO.getVideoUrl());
                lesson.setModule(module);
                return lesson;
            }).collect(Collectors.toList());
            module.setLessons(lessons);
            return module;
        }).collect(Collectors.toList());

        course.setModules(modules);
        Course savedCourse = courseRepository.save(course);
        logger.info("Course created with ID: {}", savedCourse.getId());
        return modelMapper.map(savedCourse, CourseDTO.class);
    }

    public List<CourseDTO> getAllCourses() {
        logger.info("Fetching all courses");
        return courseRepository.findAll().stream()
                .map(course -> modelMapper.map(course, CourseDTO.class))
                .collect(Collectors.toList());
    }

    public CourseDTO getCourseById(Long courseId) {
        logger.info("Fetching course with ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        return modelMapper.map(course, CourseDTO.class);
    }

    @Transactional
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        logger.info("Updating course with ID: {}", courseDTO.getId());
        if (courseDTO.getId() == null) {
            logger.error("Course ID is null");
            throw new IllegalArgumentException("Course ID must not be null");
        }

        Course existingCourse = courseRepository.findById(courseDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseDTO.getId()));

        // Update fields only if provided
        if (courseDTO.getTitle() != null) {
            existingCourse.setTitle(courseDTO.getTitle());
        }
        if (courseDTO.getShortDescription() != null) {
            existingCourse.setShortDescription(courseDTO.getShortDescription());
        }
        if (courseDTO.getFullDescription() != null) {
            existingCourse.setFullDescription(courseDTO.getFullDescription());
        }
        if (courseDTO.getLevel() != null) {
            existingCourse.setLevel(courseDTO.getLevel());
        }
        if (courseDTO.getThumbnail() != null) {
            existingCourse.setThumbnail(courseDTO.getThumbnail());
        }
        if (courseDTO.getTags() != null) {
            existingCourse.setTags(courseDTO.getTags());
        }

        // Update modules if provided
        if (courseDTO.getModules() != null) {
            logger.info("Updating modules for course ID: {}", courseDTO.getId());

            // Create a map of existing modules by ID for efficient lookup
            Map<Long, Module> existingModulesMap = existingCourse.getModules().stream()
                    .collect(Collectors.toMap(Module::getId, Function.identity(), (e1, e2) -> e1));

            // Process incoming modules
            List<Module> updatedModules = courseDTO.getModules().stream().map(moduleDTO -> {
                Module module;
                if (moduleDTO.getId() != null && existingModulesMap.containsKey(moduleDTO.getId())) {
                    // Update existing module
                    module = existingModulesMap.get(moduleDTO.getId());
                    module.setModuleTitle(moduleDTO.getModuleTitle());
                    existingModulesMap.remove(moduleDTO.getId()); // Remove to track which modules are kept
                } else {
                    // Create new module
                    module = new Module();
                    module.setModuleTitle(moduleDTO.getModuleTitle());
                    module.setCourse(existingCourse);
                }

                // Update lessons
                Map<Long, Lesson> existingLessonsMap = module.getLessons().stream()
                        .collect(Collectors.toMap(Lesson::getId, Function.identity(), (e1, e2) -> e1));
                List<Lesson> updatedLessons = moduleDTO.getLessons().stream().map(lessonDTO -> {
                    Lesson lesson;
                    if (lessonDTO.getId() != null && existingLessonsMap.containsKey(lessonDTO.getId())) {
                        // Update existing lesson
                        lesson = existingLessonsMap.get(lessonDTO.getId());
                        lesson.setLessonTitle(lessonDTO.getLessonTitle());
                        lesson.setVideoUrl(lessonDTO.getVideoUrl());
                        existingLessonsMap.remove(lessonDTO.getId());
                    } else {
                        // Create new lesson
                        lesson = new Lesson();
                        lesson.setLessonTitle(lessonDTO.getLessonTitle());
                        lesson.setVideoUrl(lessonDTO.getVideoUrl());
                        lesson.setModule(module);
                    }
                    return lesson;
                }).collect(Collectors.toList());

                // Clear lessons that are no longer referenced (will be deleted due to orphanRemoval)
                module.getLessons().clear();
                module.getLessons().addAll(updatedLessons);

                return module;
            }).collect(Collectors.toList());

            // Clear modules that are no longer referenced (will be deleted due to orphanRemoval)
            existingCourse.getModules().clear();
            existingCourse.getModules().addAll(updatedModules);
        }

        Course updatedCourse = courseRepository.save(existingCourse);
        logger.info("Course updated with ID: {}", updatedCourse.getId());
        return modelMapper.map(updatedCourse, CourseDTO.class);
    }

    public void deleteCourse(Long courseId) {
        logger.info("Deleting course with ID: {}", courseId);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        courseRepository.delete(course);
        logger.info("Course deleted with ID: {}", courseId);
    }
}