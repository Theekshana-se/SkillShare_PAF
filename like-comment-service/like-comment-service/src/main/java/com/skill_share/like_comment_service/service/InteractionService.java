package com.skill_share.like_comment_service.service;

import com.skill_share.like_comment_service.dto.CommentDTO;
import com.skill_share.like_comment_service.dto.LikeDTO;
import com.skill_share.like_comment_service.dto.PostInteractionsDTO;
import com.skill_share.like_comment_service.entity.Comment;
import com.skill_share.like_comment_service.entity.Like;
import com.skill_share.like_comment_service.exception.ResourceNotFoundException;
import com.skill_share.like_comment_service.repository.CommentRepository;
import com.skill_share.like_comment_service.repository.LikeRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InteractionService {

    private static final Logger logger = LoggerFactory.getLogger(InteractionService.class);

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public CommentDTO addComment(CommentDTO commentDTO) {
        logger.info("Adding comment for post ID: {}", commentDTO.getPostId());
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment added with ID: {}", savedComment.getId());
        return mapToCommentDTO(savedComment, commentDTO.getUserId());
    }

    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO, String currentUserId) {
        logger.info("Updating comment ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUserId().equals(currentUserId)) {
            throw new ResourceNotFoundException("You are not authorized to update this comment");
        }

        comment.setContent(commentDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);
        logger.info("Comment updated with ID: {}", updatedComment.getId());
        return mapToCommentDTO(updatedComment, currentUserId);
    }

    @Transactional
    public void deleteComment(Long commentId, String currentUserId) {
        logger.info("Deleting comment ID: {}", commentId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUserId().equals(currentUserId)) {
            throw new ResourceNotFoundException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
        logger.info("Comment deleted with ID: {}", commentId);
    }

    @Transactional
    public void addLike(LikeDTO likeDTO) {
        logger.info("Adding like for post ID: {} by user: {}", likeDTO.getPostId(), likeDTO.getUserId());
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(likeDTO.getPostId(), likeDTO.getUserId());
        if (existingLike.isPresent()) {
            logger.warn("User {} has already liked post ID: {}", likeDTO.getUserId(), likeDTO.getPostId());
            throw new IllegalArgumentException("User has already liked this post");
        }

        Like like = new Like();
        like.setPostId(likeDTO.getPostId());
        like.setUserId(likeDTO.getUserId());
        likeRepository.save(like);
        logger.info("Like added for post ID: {}", likeDTO.getPostId());
    }

    @Transactional
    public void removeLike(LikeDTO likeDTO) {
        logger.info("Removing like for post ID: {} by user: {}", likeDTO.getPostId(), likeDTO.getUserId());
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(likeDTO.getPostId(), likeDTO.getUserId());
        if (!existingLike.isPresent()) {
            logger.warn("Like not found for post ID: {} by user: {}", likeDTO.getPostId(), likeDTO.getUserId());
            throw new ResourceNotFoundException("Like not found");
        }

        likeRepository.deleteByPostIdAndUserId(likeDTO.getPostId(), likeDTO.getUserId());
        logger.info("Like removed for post ID: {}", likeDTO.getPostId());
    }

    public PostInteractionsDTO getPostInteractions(Long postId, String currentUserId) {
        logger.info("Fetching interactions for post ID: {}", postId);

        // Get likes
        int totalLikes = likeRepository.countByPostId(postId);
        boolean isLiked = likeRepository.findByPostIdAndUserId(postId, currentUserId).isPresent();

        // Get comments
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDTO> commentDTOs = comments.stream()
                .map(comment -> mapToCommentDTO(comment, currentUserId))
                .collect(Collectors.toList());
        int totalComments = commentRepository.countByPostId(postId);

        PostInteractionsDTO interactions = new PostInteractionsDTO();
        interactions.setPostId(postId);
        interactions.setTotalLikes(totalLikes);
        interactions.setIsLiked(isLiked);
        interactions.setTotalComments(totalComments);
        interactions.setComments(commentDTOs);

        logger.info("Interactions fetched for post ID: {}", postId);
        return interactions;
    }

    private CommentDTO mapToCommentDTO(Comment comment, String currentUserId) {
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        commentDTO.setTimeAgo(calculateTimeAgo(comment.getCreatedAt()));
        commentDTO.setLikes(0); // Placeholder: Add like count if implemented
        commentDTO.setIsLiked(false); // Placeholder: Add like status if implemented
        commentDTO.setIsAuthor(comment.getUserId().equals(currentUserId));
        return commentDTO;
    }

    private String calculateTimeAgo(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(createdAt, now);
        if (seconds < 60) return "Just now";
        long minutes = seconds / 60;
        if (minutes < 60) return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        long hours = minutes / 60;
        if (hours < 24) return hours + (hours == 1 ? " hour ago" : " hours ago");
        long days = hours / 24;
        return days + (days == 1 ? " day ago" : " days ago");
    }
}