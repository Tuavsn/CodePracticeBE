package com.codepractice.forum_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentRequest {
    @NotBlank(message = "Post ID is required")
    private String postId;

    @NotBlank(message = "Content is required")
    @Size(min = 1, max = 10000, message = "Content must be between 100 and 10,000 characters")
    private String content;
}
