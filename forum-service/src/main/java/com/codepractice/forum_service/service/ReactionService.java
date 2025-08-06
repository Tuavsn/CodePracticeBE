package com.codepractice.forum_service.service;

import com.codepractice.forum_service.enums.ReactionType;
import com.codepractice.forum_service.model.dto.internal.response.PostResponse;

public interface ReactionService {
    public PostResponse addOrUpdateReaction(String id, ReactionType reactionType);
}
