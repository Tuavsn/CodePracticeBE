package com.codepractice.submission_service.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunResponse {
  private String stdout;
  private String stderr;
  private String compile_output;
  private String message;
  private double time;
  private double memory;
  private boolean success;
}
