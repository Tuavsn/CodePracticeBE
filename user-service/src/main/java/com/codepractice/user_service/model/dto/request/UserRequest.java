package com.codepractice.user_service.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
	private String email;
	private String username;
	private String hashPassword;
}
