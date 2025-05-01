package com.skill_share.like_comment_service.dto;

import jakarta.validation.constraints.NotNull;

public class LikeDTO {
    @NotNull(message = "Post ID is mandatory")
    private Long postId;

    @NotNull(message = "User ID is mandatory")
    private String userId;

    // Explicit getters
    public Long getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    // Explicit setters
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}