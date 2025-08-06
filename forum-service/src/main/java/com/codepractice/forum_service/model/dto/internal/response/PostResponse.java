package com.codepractice.forum_service.model.dto.internal.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.codepractice.forum_service.model.entity.Author;
import com.codepractice.forum_service.model.entity.PostImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
    private String id;
    private Author author;
    private String title;
    private String thumbnail;
    private String content;
    private Set<PostImage> images;
    private List<String> topics;
    private long reactionCount;
    private long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
