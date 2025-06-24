package com.codepractice.common_lib.constants;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  EMAIL_ALREADY_REGISTERED(1001, "Email already registered", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
  ROLE_NOT_FOUND(1003, "Role not found", HttpStatus.NOT_FOUND);

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
