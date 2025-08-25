package com.codepractice.submission_service.model.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProblemStatsRequest {
  private String id;
  private Boolean isAccepted;
}
