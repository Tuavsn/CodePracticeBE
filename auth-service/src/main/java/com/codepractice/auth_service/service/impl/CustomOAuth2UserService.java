package com.codepractice.auth_service.service.impl;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codepractice.auth_service.enums.AccountRole;
import com.codepractice.auth_service.enums.AccountStatus;
import com.codepractice.auth_service.enums.AuthStrategy;
import com.codepractice.auth_service.model.entity.CustomOAuth2User;
import com.codepractice.auth_service.model.entity.User;
import com.codepractice.auth_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        
        String name = oauth2User.getAttribute("name");

        User user = userService.getByEmail(email);

        if (user == null) {
            userService.create(
                User.builder()
                    .username(name)
                    .email(email)
                    .role(AccountRole.USER)
                    .status(AccountStatus.ACTIVE)
                    .authStrategy(AuthStrategy.GOOGLE)
                    .build());
        }

        return new CustomOAuth2User(oauth2User, user);
    }
}
