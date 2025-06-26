package com.codepractice.user_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.user_service.enums.AccountAchievement;
import com.codepractice.user_service.enums.AccountRole;
import com.codepractice.user_service.enums.AccountStatus;
import com.codepractice.user_service.enums.AuthStrategy;
import com.codepractice.user_service.model.dto.request.UserRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.model.entity.User;
import com.codepractice.user_service.repository.UserRepository;
import com.codepractice.user_service.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserResponse save(UserRequest request) {
    request.setEmail(request.getEmail().toLowerCase());

    log.info("Attempting to save user with email: {}", request.getEmail());

    Optional<User> existUser = userRepository.findByEmail(request.getEmail());

    existUser.ifPresent(userFound -> {
      log.warn("Email already registered: {}", request.getEmail());
      throw new AppException(ErrorCode.EMAIL_ALREADY_REGISTERED);
    });

    User savedUser = userRepository.save(
        User.builder()
            .email(request.getEmail())
            .username(request.getUsername())
            .password(request.getHashPassword())
            .totalSubmissionPoint(0)
            .role(AccountRole.USER)
            .achievement(AccountAchievement.BEGINNER)
            .authStrategy(AuthStrategy.LOCAL)
            .status(AccountStatus.ACTIVE)
            .build());

    log.info("User saved successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());

    return UserResponse.builder()
        .id(savedUser.getId())
        .email(savedUser.getEmail())
        .username(savedUser.getUsername())
        .avatar(savedUser.getAvatar())
        .role(savedUser.getRole())
        .achievement(savedUser.getAchievement())
        .status(savedUser.getStatus())
        .build();
  }

  @Override
  @Transactional
  public User update(User user) {
    log.info("Attempting to update user with email: {}", user.getEmail());

    User existUser = userRepository.findByEmail(user.getEmail())
        .orElseThrow(() -> {
          log.error("User not found for update with email: {}", user.getEmail());
          return new AppException(ErrorCode.USER_NOT_FOUND);
        });

    User updatedUser = userRepository.save(updateUserDetails(existUser));
    log.info("User updated successfully with ID: {}", updatedUser.getId());
    return updatedUser;
  }

  @Override
  @Transactional
  public void block(Long id) {
    log.info("Attempting to block user with ID: {}", id);

    User user = getById(id);
    user.setStatus(AccountStatus.BLOCKED);
    userRepository.save(user);

    log.warn("User blocked successfully - ID: {}, Email: {}", user.getId(), user.getEmail());
  }

  @Override
  @Transactional
  public void hardDelete(Long id) {
    log.info("Attempting to hard delete user with ID: {}", id);

    User user = getById(id);
    userRepository.delete(user);

    log.warn("User hard deleted successfully - ID: {}, Email: {}", user.getId(), user.getEmail());
  }

  @Override
  public List<User> getAll() {
    log.debug("Retrieving all active users");

    List<User> users = userRepository.findAllByStatus(AccountStatus.ACTIVE);

    log.debug("Retrieved {} active users", users.size());
    return users;
  }

  @Override
  public User getById(Long id) {
    log.debug("Retrieving user by ID: {}", id);

    return userRepository.findById(id).orElseThrow(() -> {
      log.error("User not found with ID: {}", id);
      return new AppException(ErrorCode.USER_NOT_FOUND);
    });
  }

  @Override
  public User getByEmail(String email) {
    log.debug("Retrieving user by email: {}", email);

    return userRepository.findByEmail(email).orElseThrow(() -> {
      log.error("User not found with email: {}", email);
      return new AppException(ErrorCode.USER_NOT_FOUND);
    });
  }

  @Override
  public User getByUsername(String username) {
    log.debug("Retrieving user by username: {}", username);

    return userRepository.findByUsername(username).orElseThrow(() -> {
      log.error("User not found with username: {}", username);
      return new AppException(ErrorCode.USER_NOT_FOUND);
    });
  }

  private User updateUserDetails(User user) {
    log.debug("Updating user details for user ID: {}", user.getId());
    return user;
  }
}