package com.codepractice.user_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountRole {
  SYSTEM_ADMIN("SYSTEM_ADMIN", "System Admin"),
  USER("USER", "User");

  private final String value;
  private final String displayName;
}
