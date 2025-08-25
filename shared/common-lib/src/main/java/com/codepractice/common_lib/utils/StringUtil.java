package com.codepractice.common_lib.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringUtil {
  /**
   * Encodes a string to Base64.
   * 
   * @param input
   * @return
   */
  public static String encodeBase64(String input) {
    if (input == null)
      return null;
    return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Decodes a Base64 string.
   * 
   * @param base64
   * @return
   */
  public static String decodeBase64(String base64) {
    if (base64 == null)
      return null;
    try {
      String trimmed = base64.replaceAll("[^A-Za-z0-9+/=]", "");
      String decoded = new String(Base64.getDecoder().decode(trimmed), StandardCharsets.UTF_8);

      String extra = base64.substring(trimmed.length());
      return decoded + extra;
    } catch (IllegalArgumentException e) {
      return base64;
    }
  }
}