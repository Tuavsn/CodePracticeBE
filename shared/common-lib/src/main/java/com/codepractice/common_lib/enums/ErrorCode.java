package com.codepractice.common_lib.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  EMAIL_ALREADY_REGISTERED(1001, "Email already registered", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
  ROLE_NOT_FOUND(1003, "Role not found", HttpStatus.NOT_FOUND),
  POST_NOT_FOUND(1004, "Post not found", HttpStatus.NOT_FOUND),
  COMMENT_NOT_FOUND(1005, "Comment not found", HttpStatus.NOT_FOUND),
  PROBLEM_NOT_FOUND(1006, "Problem not found", HttpStatus.NOT_FOUND),
  UNSUPPORTED_EXECUTE_TYPE(1007, "Unsupported execute type", HttpStatus.BAD_REQUEST),
  MISMATCH_BETWEEN_SAMPLETEST_AND_RESULT(1008, "Mismatch between Testcases and results", HttpStatus.INTERNAL_SERVER_ERROR),
  INACTIVE_ACCOUNT(1009, "Account need to be active", HttpStatus.BAD_REQUEST),
  INVALID_OTP(1010, "Invalid OTP", HttpStatus.BAD_REQUEST),
  INVALID_PASSWORD_CONFIRM(1011, "Password do not match", HttpStatus.BAD_REQUEST),
  MISSING_REQUIRED_FIELDS(1012, "Missing required fields", HttpStatus.BAD_REQUEST),
  INVALID_REQUEST(1013, "Invalid request", HttpStatus.BAD_REQUEST),
  WEAK_PASSWORD(1014, "Weak password", HttpStatus.BAD_REQUEST),
  ACCOUNT_ALREADY_ACTIVE(1015, "Account already active", HttpStatus.BAD_REQUEST),
  NO_TEST_CASES_AVAILABLE(1016, "No test cases available", HttpStatus.BAD_REQUEST),
  SUBMISSION_NOT_FOUND(1017, "Submission not found", HttpStatus.NOT_FOUND),
  EXECUTION_FAILED(1018, "Execution failed", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_AUTHOR(1019, "Invalid author", HttpStatus.BAD_REQUEST);

  private final int code;
  private final String message;
  private final HttpStatusCode statusCode;

  /**
   * Join error message
   * @param args
   * @return
   */
  public String formatMessage(Object ...args) {
    return String.format(this.message, args);
  }
}
