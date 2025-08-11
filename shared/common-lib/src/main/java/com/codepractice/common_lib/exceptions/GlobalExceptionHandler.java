package com.codepractice.common_lib.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.codepractice.common_lib.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
  /**
   * Custom Exception Handler
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(AppException.class)
  public ResponseEntity<ApiResponse<Void>> handleAppException(
      AppException ex, HttpServletRequest request) {
    log.error("App exception: {}", ex.getMessage());

    ApiResponse<Void> response = ApiResponse.error(
        ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Validation Exception Handler
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    log.error("Validation error: {}", ex.getMessage());

    List<String> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());

    ApiResponse<Void> response = ApiResponse.error(
        errors,
        HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * App Exception Handler
   * @param ex
   * @param request
   * @return
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGenericException(
      Exception ex, HttpServletRequest request) {
    log.error("Unexpected error: ", ex);

    ApiResponse<Void> response = ApiResponse.error(
        "An unexpected error occurred",
        HttpStatus.INTERNAL_SERVER_ERROR.value());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
