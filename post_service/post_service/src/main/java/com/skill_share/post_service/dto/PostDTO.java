package com.skill_share.post_service.dto;

import com.skill_share.post_service.model.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class PostDTO {
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 10, max = 2000, message = "Description must be between 10 and 2000 characters")
    private String description;

    @NotNull(message = "Post type is mandatory")
    private Post.PostType postType;

    @NotBlank(message = "User ID is mandatory")
    private String userId;

    private List<String> tags = new ArrayList<>();

    private List<String> imageUrls = new ArrayList<>();

    private List<String> images = new ArrayList<>();

    // Temporary field to hold raw bytes during mapping
    private List<byte[]> imageData;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Post.PostType getPostType() { return postType; }
    public void setPostType(Post.PostType postType) { this.postType = postType; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public List<byte[]> getImageData() { return imageData; }
    public void setImageData(List<byte[]> imageData) { this.imageData = imageData; }
}