package com.codepractice.problem_service.model.entity;

import com.codepractice.problem_service.enums.ReactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reaction {
    private Author author;
    private ReactionType type;
}
