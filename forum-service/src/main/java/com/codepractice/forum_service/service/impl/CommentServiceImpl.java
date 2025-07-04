package com.codepractice.forum_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.forum_service.model.dto.external.UserResponse;
import com.codepractice.forum_service.model.dto.internal.request.CommentRequest;
import com.codepractice.forum_service.model.dto.internal.response.CommentResponse;
import com.codepractice.forum_service.model.entity.Author;
import com.codepractice.forum_service.model.entity.Comment;
import com.codepractice.forum_service.model.entity.Post;
import com.codepractice.forum_service.repository.CommentRepository;
import com.codepractice.forum_service.repository.PostRepository;
import com.codepractice.forum_service.service.CommentService;
import com.codepractice.forum_service.service.client.UserServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserServiceClient userService;

    @Override
    @Transactional
    public CommentResponse save(CommentRequest request) {
        log.info("Creating new comment for post ID: {} by user ID: {}", request.getPostId(), request.getUserId());

        try {
            UserResponse user = userService.getUserById(request.getUserId());

            Post existedPost = postRepository.findById(request.getPostId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
            
            Comment saveComment = commentRepository.save(
                Comment.builder()
                    .author(
                        Author.builder()
                            .userId(user.getId())
                            .username(user.getUsername())
                            .avatar(user.getAvatar())
                            .build()
                    )
                    .postId(existedPost.getId())
                    .content(request.getContent())
                    .isDeleted(false)
                    .build()
            );
            
            log.info("Comment created successfully with ID: {}", saveComment.getId());
            return createDTO(saveComment);

        } catch (Exception e) {
            log.error("Error creating comment for post ID: {} by user ID: {}", request.getPostId(), request.getUserId(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public CommentResponse update(String id, CommentRequest request) {
        log.info("Updating comment with ID: {}", id);

        try {
            Comment existedComment = commentRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Comment not found with ID: {}", id);
                        return new AppException(ErrorCode.COMMENT_NOT_FOUND);
                    });

            updateComment(request, existedComment);
            Comment updatedComment = commentRepository.save(existedComment);
            
            log.info("Comment updated successfully with ID: {}", id);
            return createDTO(updatedComment);

        } catch (Exception e) {
            log.error("Error updating comment ID: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(String id) {
        log.info("Soft deleting comment ID: {}", id);

        try {
            Comment existedComment = commentRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
            existedComment.setDeleted(true);
            commentRepository.save(existedComment);

        } catch (Exception e) {
            log.error("Error soft deleting comment ID: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void hardDelete(String id) {
        log.warn("Hard deleting comment ID: {}", id);

        try {
            Comment existedComment = commentRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

            commentRepository.delete(existedComment);

        } catch (Exception e) {
            log.error("Error hard deleting comment ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public List<CommentResponse> getAllByPostId(String id) {
        log.debug("Retrieving comments for post ID: {}", id);

        List<Comment> comments = commentRepository.findAllByPostIdAndIsDeleted(id, false);
        log.info("Found {} comments for post ID: {}", comments.size(), id);
        
        return comments.stream().map(comment -> createDTO(comment)).toList();
    }

    @Override
    public List<CommentResponse> getAllByUserId(String id) {
        log.debug("Retrieving comments by user ID: {}", id);

        List<Comment> comments = commentRepository.findAllByAuthor_UserIdAndIsDeleted(id, false);
        log.info("Found {} comments for user ID: {}", comments.size(), id);
        
        return comments.stream().map(comment -> createDTO(comment)).toList();
    }

    @Override
    public List<CommentResponse> getAllPersonalComments() {
        log.warn("getAllPersonalComments method called but not implemented");
        throw new UnsupportedOperationException("Unimplemented method 'getAllPersonalComments'");
    }

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

    private void updateComment(CommentRequest source, Comment target) {
        if (source.getContent() != null) {
            target.setContent(source.getContent());
        }
    }
}