package com.skill_share.like_comment_service.repository;

import com.skill_share.like_comment_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.postId = ?1")
    int countByPostId(Long postId);

    List<Comment> findByPostIdAndUserId(Long postId, String userId);
}