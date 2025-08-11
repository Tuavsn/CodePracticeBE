package com.codepractice.forum_service.model.dto.internal;

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
	private String role;
	private String achievement;
}