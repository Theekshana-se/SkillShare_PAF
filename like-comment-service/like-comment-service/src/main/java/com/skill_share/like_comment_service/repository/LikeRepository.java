package com.skill_share.like_comment_service.repository;

import com.skill_share.like_comment_service.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostIdAndUserId(Long postId, String userId);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.postId = ?1")
    int countByPostId(Long postId);

    void deleteByPostIdAndUserId(Long postId, String userId);
}