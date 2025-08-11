package com.codepractice.forum_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.forum_service.client.UserServiceClient;
import com.codepractice.forum_service.model.dto.internal.UserClientResponse;
import com.codepractice.forum_service.model.dto.request.CommentRequest;
import com.codepractice.forum_service.model.dto.response.CommentResponse;
import com.codepractice.forum_service.model.entity.Author;
import com.codepractice.forum_service.model.entity.Comment;
import com.codepractice.forum_service.model.entity.Post;
import com.codepractice.forum_service.repository.CommentRepository;
import com.codepractice.forum_service.repository.PostRepository;
import com.codepractice.forum_service.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserServiceClient userService;
    private final UserUtil userUtil;

    // ======================== COMMENT CRUD OPERATIONS ========================

    /**
     * Create new Comment
     * @param CommentRequest
     * @return Comment DTO
     */
    @Override
    @Transactional
    public CommentResponse save(CommentRequest request) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Creating new comment for post ID: {} by user ID: {}", request.getPostId(), userId);

        try {
            UserClientResponse user = userService.getUserById(userId);

            Post existedPost = postRepository.findById(request.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
            
            Comment saveComment = commentRepository.save(
                Comment.builder()
                    .author(
                        Author.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .avatar(user.getAvatar())
                            .build()
                    )
                    .postId(existedPost.getId())
                    .content(request.getContent())
                    .isDeleted(false)
                    .build()
            );

            updatePostCommentCount(existedPost, CommentCountOp.INCREASE);
            
            log.info("Comment created successfully with ID: {}", saveComment.getId());
            return createDTO(saveComment);

        } catch (Exception e) {
            log.error("Error creating comment for post ID: {} by user ID: {}", request.getPostId(), userId, e);
            throw e;
        }
    }

    /**
     * Update existed Comment
     * @param id
     * @param CommentRequest
     * @return Comment DTO
     */
    @Override
    @Transactional
    public CommentResponse update(String id, CommentRequest request) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Updating comment with ID: {}", id);

        try {
            Comment existedComment = commentRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Comment not found with ID: {}", id);
                        return new AppException(ErrorCode.COMMENT_NOT_FOUND);
                    });

            if (existedComment.getAuthor().getId() != userId) {
                throw new AppException(ErrorCode.INVALID_AUTHOR);
            }

            updateComment(request, existedComment);
            Comment updatedComment = commentRepository.save(existedComment);
            
            log.info("Comment updated successfully with ID: {}", id);
            return createDTO(updatedComment);

        } catch (Exception e) {
            log.error("Error updating comment ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Soft delete existed Comment
     * @param id
     */
    @Override
    @Transactional
    public void delete(String id) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Soft deleting comment ID: {}", id);

        try {
            Comment existedComment = commentRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

            if (existedComment.getAuthor().getId() != userId) {
                throw new AppException(ErrorCode.INVALID_AUTHOR);
            }

            existedComment.setDeleted(true);
            commentRepository.save(existedComment);

            Post existedPost = postRepository.findById(existedComment.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

            updatePostCommentCount(existedPost, CommentCountOp.DESCREASE);

        } catch (Exception e) {
            log.error("Error soft deleting comment ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Hard delete existed Comment
     * @param id
     */
    @Override
    @Transactional
    public void hardDelete(String id) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.warn("Hard deleting comment ID: {}", id);

        try {
            Comment existedComment = commentRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

            if (existedComment.getAuthor().getId() != userId) {
                throw new AppException(ErrorCode.INVALID_AUTHOR);
            }

            commentRepository.delete(existedComment);

        } catch (Exception e) {
            log.error("Error hard deleting comment ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Get all posts
     * @return List of Comment DTO
     */
    @Override
    public List<CommentResponse> getAllByPostId(String id) {
        log.debug("Retrieving comments for post ID: {}", id);

        List<Comment> comments = commentRepository.findAllByPostIdAndIsDeleted(id, false);
        log.info("Found {} comments for post ID: {}", comments.size(), id);
        
        return comments.stream().map(comment -> createDTO(comment)).toList();
    }

    /**
     * Get post by user id
     * @param userId
     * @return List of Comment DTO 
     */
    @Override
    public List<CommentResponse> getAllByUserId(String id) {
        log.debug("Retrieving comments by user ID: {}", id);

        List<Comment> comments = commentRepository.findAllByAuthor_IdAndIsDeleted(id, false);
        log.info("Found {} comments for user ID: {}", comments.size(), id);
        
        return comments.stream().map(comment -> createDTO(comment)).toList();
    }

    /**
     * Get personal comments
     * @return List of Comment DTO 
     */
    @Override
    public List<CommentResponse> getAllPersonalComments() {
        log.warn("getAllPersonalComments method called but not implemented");
        throw new UnsupportedOperationException("Unimplemented method 'getAllPersonalComments'");
    }

    // ======================== UTILS OPERATIONS ========================
    
    /**
     * Map to response DTO
     * @param source
     * @return
     */
    private CommentResponse createDTO(Comment source) {
        return CommentResponse
            .builder()
            .id(source.getId())
            .author(source.getAuthor())
            .postId(source.getPostId())
            .content(source.getContent())
            .reactions(source.getReactions())
            .createdAt(source.getCreatedAt())
            .updatedAt(source.getUpdatedAt())
            .build();
    }

    /**
     * Update fields
     * @param source
     * @param target
     */
    private void updateComment(CommentRequest source, Comment target) {
        if (source.getContent() != null) {
            target.setContent(source.getContent());
        }
    }

    /**
     * Update comment count
     * @param existedPost
     * @param op
     */
    @Transactional
    private void updatePostCommentCount(Post existedPost, CommentCountOp op) {
        long current = Optional.ofNullable(existedPost.getCommentCount()).orElse(0l);
        switch (op) {
            case INCREASE -> existedPost.setCommentCount(current + 1);
            case DESCREASE -> existedPost.setCommentCount(Math.max(0, current - 1));
        }
        postRepository.save(existedPost);
    }

    public enum CommentCountOp { INCREASE, DESCREASE}
}