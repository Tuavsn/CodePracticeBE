package com.codepractice.auth_service.service.impl;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.codepractice.auth_service.enums.AccountRole;
import com.codepractice.auth_service.model.dto.external.UserRequest;
import com.codepractice.auth_service.model.dto.external.UserResponse;
import com.codepractice.auth_service.model.entity.CustomOAuth2User;
import com.codepractice.auth_service.service.client.UserServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserServiceClient userServiceClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String providerId = oauth2User.getAttribute("sub");

        UserResponse user = userServiceClient.getUserByEmail(email);

        if (user == null) {
            userServiceClient.createUser(
                UserRequest.builder()
                    .username(name)
                    .email(email)
                    .role(AccountRole.USER)
                    .build()
            );
        }

        return new CustomOAuth2User(oauth2User, user);
    }
}
