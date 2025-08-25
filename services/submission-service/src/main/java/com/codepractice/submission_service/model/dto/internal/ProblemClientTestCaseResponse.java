package com.codepractice.submission_service.model.dto.internal;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemClientTestCaseResponse {
  private String id;
  private List<TestCase> sampleTests;
  private double timeLimitSeconds;
  private double memoryLimitMb;
  private double totalScore;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class TestCase {
    private String input;
    private String output;
    private int order;
    private double point;
  }
}
