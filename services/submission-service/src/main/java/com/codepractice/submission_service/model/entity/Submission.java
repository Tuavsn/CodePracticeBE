package com.codepractice.submission_service.model.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.codepractice.submission_service.enums.SubmitLanguage;
import com.codepractice.submission_service.enums.SubmitResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Document(collection = "submission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
public class Submission extends BaseEntity {
  private Long userId;
  private String problemId;
  private String code;
  private SubmitLanguage language;
  private SubmitResult result;
  private List<Result> resultDetails;
  private double totalTime;
  private double totalMemory;
  private double score;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  @SuperBuilder
  public static class Result extends BaseEntity {
    private SubmitResult result;
    private String token;
    private String stderr;
    private String stdout;
    private String compilerOutput;
    private String message;
    private int testCaseOrder;
    private double time;
    private double memory;
    private double point;
  }
}
