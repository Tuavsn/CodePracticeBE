package com.codepractice.user_service.model.dto.request;

import com.codepractice.user_service.enums.AccountGender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProfileRequest {
  private String avatar;
  private String phone;
  private String address;
  private String bio;
  private AccountGender gender;
}
