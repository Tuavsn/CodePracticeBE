package com.codepractice.user_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountAchievement {
  BEGINNER("beginner", "Beginner"),
  INTERMEDIATE("intermediate", "Intermediate"),
  EXPERT("expert", "Expert");

  private final String value;
  private final String displayName;
}
