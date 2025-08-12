package com.codepractice.forum_service.model.dto.request;

import java.util.List;
import java.util.Set;

import com.codepractice.forum_service.model.entity.PostImage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 10, max = 100, message = "Title must be between 10 and 100 characters")
    private String title;

    private String thumbnail;

    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 100000, message = "Content must be between 10 and 100,000 characters")
    private String content;

    @Valid
    private Set<PostImage> images;

    @Size(min = 1, message = "At least one topic is required")
    private List<@NotBlank(message = "Topic must not be blank") String> topics;
}