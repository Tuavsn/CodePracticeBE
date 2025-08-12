package com.codepractice.forum_service.model.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostImage {
    @NotBlank(message = "Image URL is required")
    // @Pattern(
    //     regexp = "^(http|https)://.*$",
    //     message = "Image URL must be a valid URL starting with http or https"
    // )
    private String url;

    @Min(value = 0, message = "Order must be zero or positive")
    private int order;
}
