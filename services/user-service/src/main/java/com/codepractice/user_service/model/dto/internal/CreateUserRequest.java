package com.codepractice.user_service.model.dto.internal;

import com.codepractice.user_service.enums.AccountRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
  private Long id;
	private String email;
	private String username;
  private AccountRole role;
}
