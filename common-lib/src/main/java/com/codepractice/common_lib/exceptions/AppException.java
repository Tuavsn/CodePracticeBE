package com.codepractice.common_lib.exceptions;

import com.codepractice.common_lib.constants.ErrorCode;

public class AppException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  private ErrorCode errorCode;

  public AppException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public AppException(ErrorCode errorCode, Object ...args) {
    super(errorCode.formatMessage(args));
    this.errorCode = errorCode;
  }
}
