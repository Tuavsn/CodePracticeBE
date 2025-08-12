package com.codepractice.problem_service.model.dto.request;

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
    @NotBlank(message = "Problem ID is required")
    private String problemId;

    @NotBlank(message = "Content is required")
    @Size(min = 100, max = 10000, message = "Content must be between 100 and 10,000 characters")
    private String content;
}
