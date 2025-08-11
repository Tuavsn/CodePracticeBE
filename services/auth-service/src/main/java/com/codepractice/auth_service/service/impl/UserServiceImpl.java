package com.codepractice.auth_service.service.impl;

import org.springframework.stereotype.Service;

import com.codepractice.auth_service.model.entity.User;
import com.codepractice.auth_service.repository.UserRepository;
import com.codepractice.auth_service.service.UserService;
import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getById(Long id) {
        log.debug("Retrieving user by ID: {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with ID: {}", id);
            return new AppException(ErrorCode.USER_NOT_FOUND);
        });

        return user;
    }

    @Override
    public User getByEmail(String email) {
        log.debug("Retrieving user by email: {}", email);

        User user = userRepository.findByEmail(email).orElse(null);

        return user;
    }

    @Override
    public User getByUsername(String username) {
        log.debug("Retrieving user by username: {}", username);

        User user = userRepository.findByUsername(username).orElse(null);

        return user;
    }

}
