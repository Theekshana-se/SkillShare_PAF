package com.skillshare.instructor_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LessonDTO {
    private Long id;

    @NotBlank(message = "Lesson title is mandatory")
    @Size(min = 3, max = 100, message = "Lesson title must be between 3 and 100 characters")
    private String lessonTitle;

    private String videoUrl;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonTitle() {
        return lessonTitle;
    }

    public void setLessonTitle(String lessonTitle) {
        this.lessonTitle = lessonTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}