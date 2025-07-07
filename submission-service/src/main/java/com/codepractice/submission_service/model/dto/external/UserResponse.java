package com.codepractice.submission_service.model.dto.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private Long id;
	private String title;
	private String username;
	private String password;
	private String role;
	private String achievement;
	private String status;
}
