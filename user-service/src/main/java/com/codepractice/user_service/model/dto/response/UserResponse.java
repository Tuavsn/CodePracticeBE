package com.codepractice.user_service.model.dto.response;

import com.codepractice.user_service.enums.AccountAchievement;
import com.codepractice.user_service.enums.AccountRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String username;
    private String avatar;
    private AccountRole role;
    private AccountAchievement achievement;
}
