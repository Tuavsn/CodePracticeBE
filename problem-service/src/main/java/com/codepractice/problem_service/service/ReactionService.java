package com.codepractice.problem_service.service;

import com.codepractice.problem_service.enums.ReactionType;
import com.codepractice.problem_service.model.dto.internal.response.ProblemResponse;

public interface ReactionService {
    public ProblemResponse addOrUpdateReaction(String id, ReactionType reactionType);
}
