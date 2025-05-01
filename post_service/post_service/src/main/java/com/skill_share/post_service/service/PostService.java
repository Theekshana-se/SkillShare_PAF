package com.skill_share.post_service.service;

import com.skill_share.post_service.dto.PostDTO;
import com.skill_share.post_service.model.Post;
import com.skill_share.post_service.repository.PostRepository;
import com.skill_share.post_service.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PostDTO createPost(PostDTO postDTO) {
        logger.info("Creating post: {}", postDTO.getTitle());
        Post post = modelMapper.map(postDTO, Post.class);
        // Map imageData directly
        if (postDTO.getImageData() != null) {
            post.setImageData(postDTO.getImageData());
        }
        Post savedPost = postRepository.save(post);
        logger.info("Post created with ID: {}", savedPost.getId());
        return modelMapper.map(savedPost, PostDTO.class);
    }

    public List<PostDTO> getAllPosts() {
        logger.info("Fetching all posts");
        return postRepository.findAll().stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long postId) {
        logger.info("Fetching post with ID: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return modelMapper.map(post, PostDTO.class);
    }

    public List<PostDTO> getPostsByUser(String userId) {
        logger.info("Fetching posts for user: {}", userId);
        return postRepository.findByUserId(userId).stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    public List<PostDTO> getPostsByType(Post.PostType postType) {
        logger.info("Fetching posts of type: {}", postType);
        return postRepository.findByPostType(postType).stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDTO updatePost(PostDTO postDTO) {
        logger.info("Updating post with ID: {}", postDTO.getId());
        if (postDTO.getId() == null) {
            logger.error("Post ID is null");
            throw new IllegalArgumentException("Post ID must not be null");
        }

        Post existingPost = postRepository.findById(postDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postDTO.getId()));

        // Update fields only if provided
        if (postDTO.getTitle() != null) {
            existingPost.setTitle(postDTO.getTitle());
        }
        if (postDTO.getDescription() != null) {
            existingPost.setDescription(postDTO.getDescription());
        }
        if (postDTO.getPostType() != null) {
            existingPost.setPostType(postDTO.getPostType());
        }
        if (postDTO.getUserId() != null) {
            existingPost.setUserId(postDTO.getUserId());
        }
        if (postDTO.getTags() != null) {
            existingPost.setTags(postDTO.getTags());
        }
        if (postDTO.getImageData() != null) {
            existingPost.setImageData(postDTO.getImageData());
        }

        Post updatedPost = postRepository.save(existingPost);
        logger.info("Post updated with ID: {}", updatedPost.getId());
        return modelMapper.map(updatedPost, PostDTO.class);
    }

    public void deletePost(Long postId) {
        logger.info("Deleting post with ID: {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        postRepository.delete(post);
        logger.info("Post deleted with ID: {}", postId);
    }
}