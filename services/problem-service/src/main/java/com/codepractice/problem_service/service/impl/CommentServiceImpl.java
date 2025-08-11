package com.codepractice.problem_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.problem_service.client.UserServiceClient;
import com.codepractice.problem_service.model.dto.internal.UserClientResponse;
import com.codepractice.problem_service.model.dto.request.CommentRequest;
import com.codepractice.problem_service.model.dto.response.CommentResponse;
import com.codepractice.problem_service.model.entity.Author;
import com.codepractice.problem_service.model.entity.Comment;
import com.codepractice.problem_service.model.entity.Problem;
import com.codepractice.problem_service.repository.CommentRepository;
import com.codepractice.problem_service.repository.ProblemRepository;
import com.codepractice.problem_service.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ProblemRepository problemRepository;
    private final UserServiceClient userService;
    private final UserUtil userUtil;

    @Override
    @Transactional
    public CommentResponse save(CommentRequest request) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Creating new comment for post ID: {} by user ID: {}", request.getProblemId(), userId);

        try {
            UserClientResponse user = userService.getUserById(userId);

            Problem existedProblem = problemRepository.findById(request.getProblemId()).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
            
            Comment saveComment = commentRepository.save(
                Comment.builder()
                    .author(
                        Author.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .avatar(user.getAvatar())
                            .build()
                    )
                    .problemId(existedProblem.getId())
                    .content(request.getContent())
                    .isDeleted(false)
                    .build()
            );

            updateProblemCommentCount(request.getProblemId(), CommentCountOp.INCREASE);
            
            log.info("Comment created successfully with ID: {}", saveComment.getId());
            return createDTO(saveComment);

        } catch (Exception e) {
            log.error("Error creating comment for post ID: {} by user ID: {}", request.getProblemId(), userId, e);
            throw e;
        }
    }

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

            updateProblemCommentCount(existedComment.getProblemId(), CommentCountOp.DECREASE);

            log.info("Comment deleted successfully with ID: {}", id);
        } catch (Exception e) {
            log.error("Error soft deleting comment ID: {}", id, e);
            throw e;
        }
    }

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

    @Override
    public List<CommentResponse> getAllByProblemId(String id) {
        log.debug("Retrieving comments for post ID: {}", id);

        List<Comment> comments = commentRepository.findAllByProblemIdAndIsDeleted(id, false);
        log.info("Found {} comments for post ID: {}", comments.size(), id);
        
        return comments.stream().map(comment -> createDTO(comment)).toList();
    }

    @Override
    public List<CommentResponse> getAllByUserId(String id) {
        log.debug("Retrieving comments by user ID: {}", id);

        List<Comment> comments = commentRepository.findAllByAuthor_IdAndIsDeleted(id, false);
        log.info("Found {} comments for user ID: {}", comments.size(), id);
        
        return comments.stream().map(comment -> createDTO(comment)).toList();
    }

    private CommentResponse createDTO(Comment source) {
        return CommentResponse
            .builder()
            .id(source.getId())
            .author(source.getAuthor())
            .problemId(source.getProblemId())
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

    @Transactional
    private void updateProblemCommentCount(String problemId, CommentCountOp op) {
        Problem existedProblem = problemRepository.findById(problemId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        long current = Optional.ofNullable(existedProblem.getCommentCount()).orElse(0l);
        switch (op) {
            case INCREASE -> existedProblem.setCommentCount(current + 1);
            case DECREASE -> existedProblem.setCommentCount(Math.max(0, current - 1));
        }
        problemRepository.save(existedProblem);
    }

    public enum CommentCountOp { INCREASE, DECREASE }
}
