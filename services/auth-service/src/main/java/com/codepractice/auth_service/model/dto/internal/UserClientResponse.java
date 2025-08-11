package com.codepractice.auth_service.model.dto.internal;

import com.codepractice.auth_service.enums.AccountRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserClientResponse {
	private Long id;
	private String email;
	private String avatar;
	private String username;
	private AccountRole role;
	private String achievement;
}