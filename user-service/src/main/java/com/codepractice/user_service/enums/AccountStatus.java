package com.codepractice.user_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {
  ACTIVE("ACTIVE", "active"),
  INACTIVE("INACTIVE", "inactive"),
  BLOCKED("BLOCKED", "blocked");

  private final String value;
  private final String displayName;
}
