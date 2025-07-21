package com.codepractice.auth_service.model.dto.external;

import com.codepractice.auth_service.enums.AccountRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private Long id;
	private String email;
	private String username;
	private AccountRole role;
}
