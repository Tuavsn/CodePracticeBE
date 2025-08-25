package com.codepractice.submission_service.model.dto.response;

import java.time.LocalDateTime;

import com.codepractice.submission_service.enums.SubmitResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultResponse {
  private String submissionId;
  private SubmitResult result;
  private String error;
  private String stdout;
  private String compilerOutput;
  private String message;
  private double time;
  private double memory;
  private double point;
  private LocalDateTime createdAt;
}
