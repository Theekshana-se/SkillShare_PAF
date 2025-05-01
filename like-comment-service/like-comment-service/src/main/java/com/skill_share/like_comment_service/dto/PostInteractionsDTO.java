package com.skill_share.like_comment_service.dto;

import java.util.List;

public class PostInteractionsDTO {
    private Long postId;
    private int totalLikes;
    private boolean isLiked;
    private int totalComments;
    private List<CommentDTO> comments;

    // Getters
    public Long getPostId() {
        return postId;
    }

    public int getTotalLikes() {
        return totalLikes;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    // Explicit setters
    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setTotalLikes(int totalLikes) {
        this.totalLikes = totalLikes;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }
}