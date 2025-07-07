package com.codepractice.problem_service.model.entity;

import java.util.List;

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
    private String constraints;
    private ProblemDifficulty difficulty;
    private List<String> example;
    private List<String> tags;
    private List<String> hints;
    private List<CodeTemplate> codeTemplates;
    private List<TestCase> sampleTests;
    private long totalSubmissions;
    private long totalAcceptedSubmissions;
    private double timeLimitSeconds;
    private double memoryLimitMb;
    private double totalScore;
}
