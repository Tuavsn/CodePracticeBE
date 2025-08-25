package com.codepractice.submission_service.model.dto.request;

import com.codepractice.submission_service.enums.SubmitLanguage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExecuteRequest {
  private String code;
  private String input;
  private SubmitLanguage language;
  private String expectedOutput;
  private int testCaseOrder;
  private double testCasePoint;
}
