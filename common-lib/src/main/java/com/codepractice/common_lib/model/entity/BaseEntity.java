package com.codepractice.common_lib.model.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseEntity<T extends BaseEntityInterface> implements BaseEntityInterface {
  protected String id;
  protected String createdBy;
  protected String updatedBy;
  protected LocalDateTime createdAt;
  protected LocalDateTime updatedAt;
  protected boolean isDeleted;
  protected Long version;

  @Override
  public String toString() {
    return "BaseEntity [id=" + id + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdAt=" + createdAt
        + ", updatedAt=" + updatedAt + ", isDeleted=" + isDeleted + ", version=" + version + "]";
  }

  @Override
  public void softDelete() {
    this.isDeleted = true;
    this.updatedAt = LocalDateTime.now();
  }

  @Override
  public void restore() {
    this.isDeleted = false;
    this.updatedAt = LocalDateTime.now();
  }
}
