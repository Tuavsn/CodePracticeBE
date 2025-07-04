package com.codepractice.problem_service.model.dto.internal.response;

import java.time.LocalDateTime;
import java.util.Set;

import com.codepractice.problem_service.model.entity.Author;
import com.codepractice.problem_service.model.entity.Reaction;

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
    private String problemId;
    private String content;
    private Set<Reaction> reactions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
