package com.skill_share.like_comment_service.controller;

import com.skill_share.like_comment_service.dto.CommentDTO;
import com.skill_share.like_comment_service.dto.LikeDTO;
import com.skill_share.like_comment_service.dto.PostInteractionsDTO;
import com.skill_share.like_comment_service.service.InteractionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class InteractionController {

    private static final Logger logger = LoggerFactory.getLogger(InteractionController.class);

    @Autowired
    private InteractionService interactionService;

    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> addComment(@Valid @RequestBody CommentDTO commentDTO) {
        logger.info("Received request to add comment for post ID: {}", commentDTO.getPostId());
        CommentDTO createdComment = interactionService.addComment(commentDTO);
        return ResponseEntity.ok(createdComment);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentDTO commentDTO,
            @RequestParam String currentUserId) {
        logger.info("Received request to update comment ID: {}", commentId);
        CommentDTO updatedComment = interactionService.updateComment(commentId, commentDTO, currentUserId);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam String currentUserId) {
        logger.info("Received request to delete comment ID: {}", commentId);
        interactionService.deleteComment(commentId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/likes")
    public ResponseEntity<Void> addLike(@Valid @RequestBody LikeDTO likeDTO) {
        logger.info("Received request to add like for post ID: {}", likeDTO.getPostId());
        interactionService.addLike(likeDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Void> removeLike(@Valid @RequestBody LikeDTO likeDTO) {
        logger.info("Received request to remove like for post ID: {}", likeDTO.getPostId());
        interactionService.removeLike(likeDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}/interactions")
    public ResponseEntity<PostInteractionsDTO> getPostInteractions(
            @PathVariable Long postId,
            @RequestParam String currentUserId) {
        logger.info("Received request to fetch interactions for post ID: {}", postId);
        PostInteractionsDTO interactions = interactionService.getPostInteractions(postId, currentUserId);
        return ResponseEntity.ok(interactions);
    }
}