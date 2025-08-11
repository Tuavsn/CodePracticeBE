package com.codepractice.forum_service.model.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.codepractice.forum_service.model.entity.Author;
import com.codepractice.forum_service.model.entity.Reaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private String id;
    private Author author;
    private String postId;
    private String content;
    private Set<Reaction> reactions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
