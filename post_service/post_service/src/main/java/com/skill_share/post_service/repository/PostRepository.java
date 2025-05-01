package com.skill_share.post_service.repository;

import com.skill_share.post_service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(String userId);
    List<Post> findByPostType(Post.PostType postType);
    List<Post> findByUserIdAndPostType(String userId, Post.PostType postType);
}