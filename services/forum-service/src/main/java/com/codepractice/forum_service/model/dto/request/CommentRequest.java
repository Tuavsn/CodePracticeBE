package com.codepractice.forum_service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(
        regexp = "^[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}$",
        message = "Post ID must be a valid UUID"
    )
    private String postId;

    @NotBlank(message = "Content is required")
    @Size(min = 100, max = 10000, message = "Content must be between 100 and 10,000 characters")
    private String content;
}
