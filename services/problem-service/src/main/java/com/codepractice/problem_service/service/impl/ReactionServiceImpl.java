package com.codepractice.problem_service.service.impl;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.problem_service.client.UserServiceClient;
import com.codepractice.problem_service.enums.ReactionType;
import com.codepractice.problem_service.model.dto.internal.UserClientResponse;
import com.codepractice.problem_service.model.dto.response.ProblemResponse;
import com.codepractice.problem_service.model.entity.Author;
import com.codepractice.problem_service.model.entity.Problem;
import com.codepractice.problem_service.model.entity.Reaction;
import com.codepractice.problem_service.repository.ProblemRepository;
import com.codepractice.problem_service.service.ReactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ProblemRepository problemRepository;
    private final UserServiceClient userService;
    private final UserUtil userUtil;

    // ======================== REACTION CRUD OPERATIONS ========================

    /**
     * Add or update reaction to a problem
     * @param problemId The ID of the problem
     * @param reactionType The type of reaction (LIKE/DISLIKE)
     * @return problem DTO
     */
    @Override
    @Transactional
    public ProblemResponse addOrUpdateReaction(String id, ReactionType reactionType) {
        Long userId = Long.parseLong(userUtil.getCurrentUserId());

        log.info("Adding/updating reaction for problem: {} by user: {} with type: {}",
                id, userId, reactionType);

        Problem problem = problemRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PROBLEM_NOT_FOUND));

        if (problem.getReactions() == null) {
            problem.setReactions(new HashSet<>());
        }

        Optional<Reaction> existedReaction = problem.getReactions().stream()
                .filter(reaction -> reaction.getAuthor().getId() == userId).findFirst();

        if (existedReaction.isPresent()) {
            Reaction reaction = existedReaction.get();
            if (reaction.getType().equals(reactionType)) {
                problem.getReactions().remove(reaction);
                log.info("Removed reaction for problem: {} by user: {}", id, userId);
            } else {
                reaction.setType(reactionType);
                log.info("Updated reaction for problem: {} by user: {} to type: {}",
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
            problem.getReactions().add(newReaction);
            log.info("Updated reaction for problem: {} by user: {} to type: {}", id, user.getId(), reactionType);
        }

        problem.setReactionCount(problem.getReactions().size());

        Problem updateProblem = problemRepository.save(problem);

        return createDTO(updateProblem);
    }

    // ======================== UTILS OPERATIONS ========================

    /**
     * Map to response DTO
     * 
     * @param source
     * @return
     */
    private ProblemResponse createDTO(Problem source) {
        return ProblemResponse
                .builder()
                .id(source.getId())
                .title(source.getTitle())
                .description(source.getDescription())
                .thumbnail(source.getThumbnail())
                .constraints(source.getConstraints())
                .difficulty(source.getDifficulty())
                .examples(source.getExamples())
                .tags(source.getTags())
                .hints(source.getHints())
                .codeTemplates(source.getCodeTemplates())
                .reactions(source.getReactions())
                .reactionCount(source.getReactionCount())
                .commentCount(source.getCommentCount())
                .totalSubmissions(source.getTotalSubmissions())
                .totalAcceptedSubmissions(source.getTotalAcceptedSubmissions())
                .timeLimitSeconds(source.getTimeLimitSeconds())
                .memoryLimitMb(source.getMemoryLimitMb())
                .totalScore(source.getTotalScore())
                .createdAt(source.getCreatedAt())
                .updatedAt(source.getUpdatedAt())
                .build();
    }
}
