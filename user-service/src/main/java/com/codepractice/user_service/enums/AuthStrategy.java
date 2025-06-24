package com.codepractice.user_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthStrategy {
  LOCAL("LOCAL", "Local"),
  GOOGLE("GOOGLE", "Google");

  private final String value;
  private final String displayName;
}
