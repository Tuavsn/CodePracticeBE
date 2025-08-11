package com.codepractice.user_service.model.dto.request;

import com.codepractice.user_service.enums.AccountRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
	private Long id;
	private String email;
	private String username;
	private String avatar;
	private String phone;
	private String address;
	private String bio;
	private AccountRole role;
}
