package com.codepractice.auth_service.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.codepractice.auth_service.enums.AccountStatus;
import com.codepractice.auth_service.model.entity.User;
import com.codepractice.auth_service.service.UserService;
import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        if (user.getStatus() != AccountStatus.ACTIVE) {
            throw new AppException(ErrorCode.INACTIVE_ACCOUNT);
        }

        return user;
    }
}
