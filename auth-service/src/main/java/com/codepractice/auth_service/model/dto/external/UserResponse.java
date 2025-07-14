package com.codepractice.auth_service.model.dto.external;

import com.codepractice.auth_service.enums.AccountRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private Long id;
	private String email;
	private String avatar;
	private String username;
	private String password;
	private AccountRole role;
	private String achievement;
	private String status;
}