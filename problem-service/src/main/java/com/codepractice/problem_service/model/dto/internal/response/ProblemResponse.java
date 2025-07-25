package com.codepractice.problem_service.model.dto.internal.response;

import java.time.LocalDateTime;
import java.util.List;

import com.codepractice.problem_service.enums.ProblemDifficulty;
import com.codepractice.problem_service.model.entity.CodeExample;
import com.codepractice.problem_service.model.entity.CodeTemplate;
import com.codepractice.problem_service.model.entity.TestCase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemResponse {
    private String id;
    private String title;
    private String description;
    private String thumbnail;
    private ProblemDifficulty difficulty;
    private List<String> constraints;
    private List<CodeExample> examples;
    private List<String> tags;
    private List<String> hints;
    private List<CodeTemplate> codeTemplates;
    private List<TestCase> sampleTests;
    private long reactionCount;
    private long commentCount;
    private long totalSubmissions;
    private long totalAcceptedSubmissions;
    private double timeLimitSeconds;
    private double memoryLimitMb;
    private double totalScore;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
}
