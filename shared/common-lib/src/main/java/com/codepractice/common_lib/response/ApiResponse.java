package com.codepractice.common_lib.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
  private boolean success;
  private String message;
  private int status;
  private T data;
  private List<String> errors;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;

  private Pagination pagination;

  static class Pagination {
    private int page;
    private int limit;
    private int total;
  }

  /**
   * Build success Repsonse
   * @param <T>
   * @param data
   * @return
   */
  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder()
      .success(true)
      .data(data)
      .timestamp(LocalDateTime.now())
      .build();
  }

  /**
   * Build success Repsonse
   * @param <T>
   * @param data
   * @param message
   * @return
   */
  public static <T> ApiResponse<T> success(T data, String message) {
    return ApiResponse.<T>builder()
      .success(true)
      .message(message)
      .data(data)
      .timestamp(LocalDateTime.now())
      .build();
  }

  /**
   * Build success Repsonse
   * @param <T>
   * @param data
   * @param message
   * @param status
   * @return
   */
  public static <T> ApiResponse<T> success(T data, String message, int status) {
    return ApiResponse.<T>builder()
      .success(true)
      .message(message)
      .status(status)
      .data(data)
      .timestamp(LocalDateTime.now())
      .build();
  }

  /**
   * Build error Repsonse
   * @param <T>
   * @param message
   * @param status
   * @return
   */
  public static <T> ApiResponse<T> error(String message, int status) {
    return ApiResponse.<T>builder()
      .success(false)
      .message(message)
      .status(status)
      .timestamp(LocalDateTime.now())
      .build();
  }

  /**
   * Build error Repsonse
   * @param <T>
   * @param errors
   * @param status
   * @return
   */
  public static <T> ApiResponse<T> error(List<String> errors, int status) {
    return ApiResponse.<T>builder()
      .success(false)
      .errors(errors)
      .status(status)
      .timestamp(LocalDateTime.now())
      .build();
  }

  /**
   * Build error Repsonse
   * @param <T>
   * @param errors
   * @param status
   * @return
   */
  public static <T> ApiResponse<T> error(List<String> errors, String message, int status) {
    return ApiResponse.<T>builder()
      .success(false)
      .message(message)
      .status(status)
      .errors(errors)
      .timestamp(LocalDateTime.now())
      .build();
  }
}
