package com.codepractice.forum_service.service.impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.forum_service.model.dto.external.UserResponse;
import com.codepractice.forum_service.model.dto.internal.request.PostRequest;
import com.codepractice.forum_service.model.dto.internal.response.PostResponse;
import com.codepractice.forum_service.model.entity.Author;
import com.codepractice.forum_service.model.entity.Post;
import com.codepractice.forum_service.repository.PostRepository;
import com.codepractice.forum_service.service.PostService;
import com.codepractice.forum_service.service.client.UserServiceClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserServiceClient userService;
    private final UserUtil userUtil;

    // ======================== POST CRUD OPERATIONS ========================

    /**
     * Create new Post
     * @param PostRequest
     * @return Post DTO
     */
    @Override
    @Transactional
    public PostResponse save(PostRequest request) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Creating new post for user ID: {}", userId);

        try {
            UserResponse user = userService.getUserById(userId);

            Post savedPost = postRepository.save(
                    Post.builder()
                            .author(
                                    Author.builder()
                                            .id(user.getId())
                                            .username(user.getUsername())
                                            .avatar(user.getAvatar())
                                            .build())
                            .title(request.getTitle())
                            .thumbnail(request.getThumbnail())
                            .topics(request.getTopics())
                            .content(request.getContent())
                            .images(request.getImages())
                            .reactions(new HashSet<>())
                            .reactionCount(0)
                            .commentCount(0)
                            .isDeleted(false)
                            .build());

            log.info("Post created successfully with ID: {}", savedPost.getId());
            return createDTO(savedPost);

        } catch (Exception e) {
            log.error("Error creating post for user ID: {}", userId, e);
            throw e;
        }
    }

    /**
     * Update existed Post
     * @param id
     * @param PostRequest
     * @return Post DTO
     */
    @Override
    @Transactional
    public PostResponse update(String id, PostRequest request) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Updating post with ID: {}", id);

        try {
            Post existedPost = postRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Post not found with ID: {}", id);
                        return new AppException(ErrorCode.POST_NOT_FOUND);
                    });

            if (existedPost.getAuthor().getId() != userId) {
                throw new AppException(ErrorCode.INVALID_AUTHOR);
            }

            updatePost(request, existedPost);
            Post updatedPost = postRepository.save(existedPost);

            log.info("Post updated successfully with ID: {}", id);
            return createDTO(updatedPost);

        } catch (Exception e) {
            log.error("Error updating post ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Soft delete existed Post
     * @param id
     */
    @Override
    @Transactional
    public void delete(String id) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Soft deleting post ID: {}", id);

        try {
            Post existedPost = postRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

            if (existedPost.getAuthor().getId() != userId) {
                throw new AppException(ErrorCode.INVALID_AUTHOR);
            }

            existedPost.setDeleted(true);
            postRepository.save(existedPost);

        } catch (Exception e) {
            log.error("Error soft deleting post ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Hard delete existed Post
     * @param id
     */
    @Override
    @Transactional
    public void hardDelete(String id) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.warn("Hard deleting post ID: {}", id);

        try {
            Post existedPost = postRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

            if (existedPost.getAuthor().getId() != userId) {
                throw new AppException(ErrorCode.INVALID_AUTHOR);
            }

            postRepository.delete(existedPost);

        } catch (Exception e) {
            log.error("Error hard deleting post ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Get all posts
     * @return List of Post DTO
     */
    @Override
    public List<PostResponse> getAll() {
        log.debug("Retrieving all active posts");

        List<Post> posts = postRepository.findAllByIsDeleted(false);
        log.info("Found {} active posts", posts.size());

        return posts.stream().map(post -> createDTO(post)).toList();
    }

    /**
     * Get post by user id
     * @param userId
     * @return List of Post DTO 
     */
    @Override
    public List<PostResponse> getByUserId(String userId) {
        log.debug("Retrieving posts by user ID: {}", userId);

        List<Post> posts = postRepository.findAllByAuthor_Id(userId);
        log.info("Found {} posts for user ID: {}", posts.size(), userId);

        return posts.stream().map(post -> createDTO(post)).toList();
    }

    /**
     * Get post by id
     * @param id
     * @return Post DTO 
     */
    @Override
    public PostResponse getById(String id) {
        log.debug("Retrieving post by ID: {}", id);

        Post post = postRepository.findById(id).orElseThrow(() -> {
            log.error("Post not found with ID: {}", id);
            return new AppException(ErrorCode.POST_NOT_FOUND);
        });

        return createDTO(post);
    }

    // ======================== UTILS OPERATIONS ========================

    /**
     * Map to response DTO
     * @param source
     * @return
     */
    private PostResponse createDTO(Post source) {
        return PostResponse
                .builder()
                .id(source.getId())
                .author(source.getAuthor())
                .title(source.getTitle())
                .thumbnail(source.getThumbnail())
                .topics(source.getTopics())
                .content(source.getContent())
                .images(source.getImages())
                .reactions(source.getReactions())
                .reactionCount(source.getReactionCount())
                .commentCount(source.getCommentCount())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }

    /**
     * Update fields
     * @param source
     * @param target
     */
    private void updatePost(PostRequest source, Post target) {
        if (source.getTitle() != null) {
            target.setTitle(source.getTitle());
        }
        if (source.getContent() != null) {
            target.setContent(source.getContent());
        }
        if (source.getThumbnail() != null) {
            target.setThumbnail(source.getThumbnail());
        }
        if (source.getTopics() != null && !source.getTopics().isEmpty()) {
            target.setTopics(source.getTopics());
        }
        if (source.getImages() != null && !source.getImages().isEmpty()) {
            target.setImages(source.getImages());
        }
    }
}