package com.codepractice.user_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codepractice.common_lib.constants.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.user_service.enums.AccountStatus;
import com.codepractice.user_service.model.entity.User;
import com.codepractice.user_service.repository.UserRepository;
import com.codepractice.user_service.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User save(User user) {
    Optional<User> existUser = userRepository.findByEmail(user.getEmail());
    existUser.ifPresent(userFound -> {
      throw new AppException(ErrorCode.EMAIL_ALREADY_REGISTERED);
    });
    return userRepository.save(user);
  }

  @Override
  public User update(User user) {
    User existUser = userRepository.findByEmail(user.getEmail())
        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    return userRepository.save(updateUserDetails(existUser));
  }

  @Override
  public void delete(String id) {
    User user = getById(id);
    user.setDeleted(true);
    userRepository.save(user);
  }

  @Override
  public void hardDelete(String id) {
    User user = getById(id);
    userRepository.delete(user);
  }

  @Override
  public List<User> getAll() {
    return userRepository.findAllByStatus(AccountStatus.ACTIVE);
  }

  @Override
  public User getById(String id) {
    return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public User getByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
  }

  @Override
  public User getByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
  }

  private User updateUserDetails(User user) {
    return user;
  }
}
