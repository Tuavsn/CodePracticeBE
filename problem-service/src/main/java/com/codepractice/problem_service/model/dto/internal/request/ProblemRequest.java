package com.codepractice.problem_service.model.dto.internal.request;

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
public class ProblemRequest {
    private String title;
    private String description;
    private String thumbnail;
    private ProblemDifficulty difficulty;
    private List<String> constraints;
    private List<CodeExample> example;
    private List<String> tags;
    private List<String> hints;
    private List<CodeTemplate> codeTemplates;
    private List<TestCase> sampleTests;
    private double timeLimitSeconds;
    private double memoryLimitMb;
    private double totalScore;
}
