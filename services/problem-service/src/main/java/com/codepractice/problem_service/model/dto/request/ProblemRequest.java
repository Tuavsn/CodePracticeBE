package com.codepractice.problem_service.model.dto.request;

import java.util.List;

import com.codepractice.problem_service.enums.ProblemDifficulty;
import com.codepractice.problem_service.model.entity.CodeExample;
import com.codepractice.problem_service.model.entity.CodeTemplate;
import com.codepractice.problem_service.model.entity.TestCase;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemRequest {
    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 20000, message = "Description must be between 10 and 20,000 characters")
    private String description;

    private String thumbnail;

    @NotNull(message = "Difficulty is required")
    private ProblemDifficulty difficulty;

    @Size(max = 100, message = "Constraints list size must not exceed 100")
    private List<@NotBlank(message = "Constraint must not be blank") String> constraints;

    @Valid
    @Size(min = 1, message = "At least one example is required")
    private List<CodeExample> examples;

    @Size(max = 10, message = "Tags list size must not exceed 10")
    private List<@NotBlank(message = "Tag must not be blank") String> tags;

    @Size(max = 10, message = "Hints list size must not exceed 10")
    private List<@NotBlank(message = "Hint must not be blank") String> hints;

    @Valid
    private List<CodeTemplate> codeTemplates;

    @Valid
    @Size(min = 1, message = "At least one sample test is required")
    private List<TestCase> sampleTests;

    @DecimalMin(value = "0.1", inclusive = true, message = "Time limit must be at least 0.1 seconds")
    private double timeLimitSeconds;

    @DecimalMin(value = "1", inclusive = true, message = "Memory limit must be at least 1 MB")
    private double memoryLimitMb;

    @DecimalMin(value = "0", inclusive = false, message = "Total score must be positive")
    private double totalScore;
}
