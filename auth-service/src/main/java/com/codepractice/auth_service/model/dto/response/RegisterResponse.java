package com.codepractice.auth_service.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private String email;
	private String username;
	private String avatar;
	private String role;
	private String achievement;
	private String status;
    private String acc_token;
    private String rf_token;
}
