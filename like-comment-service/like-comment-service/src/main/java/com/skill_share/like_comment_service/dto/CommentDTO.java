package com.skill_share.like_comment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDTO {
    private Long id;

    @NotNull(message = "Post ID is mandatory")
    private Long postId;

    @NotNull(message = "User ID is mandatory")
    private String userId;

    @NotBlank(message = "User name is mandatory")
    private String userName;

    @NotBlank(message = "User username is mandatory")
    private String userUsername;

    private String userAvatarUrl;

    @NotBlank(message = "Content is mandatory")
    private String content;

    private String timeAgo;

    private int likes;

    private boolean isLiked;

    private boolean isAuthor;

    // Explicit getters
    public Long getId() {
        return id;
    }

    public Long getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public String getContent() {
        return content;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    // Explicit setters (already defined previously)
    public void setId(Long id) {
        this.id = id;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public void setIsAuthor(boolean isAuthor) {
        this.isAuthor = isAuthor;
    }
}