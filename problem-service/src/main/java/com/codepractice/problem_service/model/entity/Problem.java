package com.codepractice.problem_service.model.entity;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.codepractice.problem_service.enums.ProblemDifficulty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "problem")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Problem extends BaseEntity {
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
    private Set<Reaction> reactions;
    private long reactionCount;
    private long commentCount;
    private long totalSubmissions;
    private long totalAcceptedSubmissions;
    private double timeLimitSeconds;
    private double memoryLimitMb;
    private double totalScore;
}
