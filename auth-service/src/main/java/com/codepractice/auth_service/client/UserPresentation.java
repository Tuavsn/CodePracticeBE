package com.codepractice.auth_service.client;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPresentation {
	private Long id;
	private String email;
	private String avatar;
	private String username;
	private String password;
	private String role;
	private String achievement;
	private String status;
}