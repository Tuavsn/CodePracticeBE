package com.codepractice.forum_service.model.dto.request;

import java.util.List;
import java.util.Set;

import com.codepractice.forum_service.model.entity.PostImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    private String title;
    private String thumbnail;
    private String content;
    private Set<PostImage> images;
    private List<String> topics;
}