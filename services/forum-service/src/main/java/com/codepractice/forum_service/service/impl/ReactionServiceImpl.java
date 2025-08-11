package com.codepractice.forum_service.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.forum_service.client.UserServiceClient;
import com.codepractice.forum_service.enums.ReactionType;
import com.codepractice.forum_service.model.dto.internal.UserClientResponse;
import com.codepractice.forum_service.model.dto.response.PostResponse;
import com.codepractice.forum_service.model.entity.Author;
import com.codepractice.forum_service.model.entity.Post;
import com.codepractice.forum_service.model.entity.Reaction;
import com.codepractice.forum_service.repository.PostRepository;
import com.codepractice.forum_service.service.ReactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final PostRepository postRepository;
    private final UserServiceClient userService;
    private final UserUtil userUtil;

    // ======================== REACTION CRUD OPERATIONS ========================

    /**
     * Add or update reaction to a post
     * @param postId The ID of the post
     * @param reactionType The type of reaction (LIKE/DISLIKE)
     * @return Post DTO
     */
    @Override
    @Transactional
    public PostResponse addOrUpdateReaction(String id, ReactionType reactionType) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Adding/updating reaction for post: {} by user: {} with type: {}",
                id, userId, reactionType);

        Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Optional<Reaction> existedReaction = post.getReactions().stream()
                .filter(reaction -> reaction.getAuthor().getId() == userId).findFirst();

        if (existedReaction.isPresent()) {
            Reaction reaction = existedReaction.get();
            if (reaction.getType().equals(reactionType)) {
                post.getReactions().remove(reaction);
                log.info("Removed reaction for post: {} by user: {}", id, userId);
            } else {
                reaction.setType(reactionType);
                log.info("Updated reaction for post: {} by user: {} to type: {}",
                        id, userId, reactionType);
            }
        } else {
            UserClientResponse user = userService.getUserById(userId);
            Reaction newReaction = Reaction.builder()
                    .author(Author.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .avatar(user.getAvatar())
                            .build())
                    .type(reactionType)
                    .build();
            post.getReactions().add(newReaction);
            log.info("Updated reaction for post: {} by user: {} to type: {}", id, user.getId(), reactionType);
        }

        post.setReactionCount(post.getReactions().size());

        Post updatePost = postRepository.save(post);

        return createDTO(updatePost);
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
    
}
