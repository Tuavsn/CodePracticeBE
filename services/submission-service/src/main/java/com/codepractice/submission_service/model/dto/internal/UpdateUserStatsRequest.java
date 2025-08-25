package com.codepractice.submission_service.model.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserStatsRequest {
  private Long id;
  private double newAchievePoint;
}
