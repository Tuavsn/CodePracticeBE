package com.codepractice.submission_service.enums;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SubmitResult {
  IN_QUEUE(1, "in_queue", "In Queue"),
  PROCESSING(2, "processing", "Processing"),
  ACCEPTED(3, "accepted", "Accepted"),
  WRONG_ANSWER(4, "wrong_answer", "Wrong Answer"),
  TIME_LIMIT(5, "time_limit_exceeded", "Time Limit Exceeded"),
  COMPILE_ERROR(6, "compilation_error", "Compliation Error"),
  RUNTIME_ERROR(7, "runtime_error", "Runtime Error");

  private final int code;
  private final String name;
  private final String displayName;

  public static SubmitResult getByCode(int code) {
    return Arrays.stream(values())
        .filter(result -> result.getCode() == code)
        .findFirst()
        .orElseThrow();
  }

  public static String getDisplayNameByCode(int code) {
    return Arrays.stream(values())
        .filter(result -> result.getCode() == code)
        .map(SubmitResult::getDisplayName)
        .findFirst()
        .orElse("Unknown Code"); // Trả về "Unknown Code" nếu không tìm thấy
  }
}
