package com.skill_share.post_service.controller;

import com.skill_share.post_service.dto.PostDTO;
import com.skill_share.post_service.model.Post;
import com.skill_share.post_service.service.PostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @PostMapping(value = "/addpost", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PostDTO> addPost(
            @RequestPart("post") @Valid PostDTO postDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        logger.info("Received addPost request with post: {}, images: {}", postDTO, images != null ? images.size() : "none");

        if (images != null && !images.isEmpty()) {
            // Convert images to raw bytes
            List<byte[]> imageBytes = images.stream()
                    .map(file -> {
                        try {
                            return file.getBytes();
                        } catch (Exception e) {
                            logger.error("Error reading image bytes: {}", file.getOriginalFilename(), e);
                            throw new RuntimeException("Failed to read image bytes", e);
                        }
                    })
                    .collect(Collectors.toList());
            postDTO.setImageData(imageBytes); // Store raw bytes in DTO (temporary field)
        }

        PostDTO createdPost = postService.createPost(postDTO);
        return ResponseEntity.ok(createdPost);
    }

    // Other endpoints unchanged
    @GetMapping("/getposts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUser(@PathVariable String userId) {
        return ResponseEntity.ok(postService.getPostsByUser(userId));
    }

    @GetMapping("/posts/type/{postType}")
    public ResponseEntity<List<PostDTO>> getPostsByType(@PathVariable Post.PostType postType) {
        return ResponseEntity.ok(postService.getPostsByType(postType));
    }

    @PutMapping("/updatepost/{postId}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long postId, @Valid @RequestBody PostDTO postDTO) {
        if (postId <= 0) {
            throw new IllegalArgumentException("Invalid post ID: " + postId);
        }
        postDTO.setId(postId);
        return ResponseEntity.ok(postService.updatePost(postDTO));
    }

    @DeleteMapping("/deletepost/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}