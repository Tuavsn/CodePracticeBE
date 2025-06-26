package com.codepractice.common_lib.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseResponseDTO {
  protected String id;
  protected String createdBy;
  protected String updatedBy;
  protected LocalDateTime createdAt;
  protected LocalDateTime updatedAt;
  protected boolean isDeleted;
  protected Long version;
}
