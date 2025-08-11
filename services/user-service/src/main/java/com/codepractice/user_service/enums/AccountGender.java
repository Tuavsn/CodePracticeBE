package com.codepractice.user_service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountGender {
  MALE("male", "Male"),
  FEMALE("female", "Female");

  private final String value;
  private final String displayName;
}
