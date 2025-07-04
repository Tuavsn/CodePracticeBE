package com.codepractice.forum_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReactionType {
    LIKE("LIKE", "Like"),
    DISLIKE("DISLIKE", "Dislike");

    private final String name;
    private final String displayName;
}
