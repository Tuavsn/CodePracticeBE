package com.codepractice.user_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.user_service.enums.AccountAchievement;
import com.codepractice.user_service.model.dto.internal.CreateUserRequest;
import com.codepractice.user_service.model.dto.internal.UpdateUserStatsRequest;
import com.codepractice.user_service.model.dto.request.UpdateProfileRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.model.entity.User;
import com.codepractice.user_service.model.mapper.UserMapper;
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
  private final UserMapper userMapper;
  private final UserUtil userUtil;

  /**
   * Get all users
   * 
   * @return list of users
   */
  @Override
  public List<UserResponse> getAll() {
    log.debug("Retrieving all users");

    List<User> users = userRepository.findAll();

    log.debug("Retrieved {} users", users.size());

    return users.stream().map(user -> userMapper.toDTO(user)).toList();
  }

  /**
   * Get user by ID
   * 
   * @param id
   * @return user info
   */
  @Override
  public UserResponse getById(Long id) {
    log.debug("Retrieving user by ID: {}", id);

    User user = userRepository.findById(id).orElseThrow(() -> {
      log.error("User not found with ID: {}", id);
      return new AppException(ErrorCode.USER_NOT_FOUND);
    });

    return userMapper.toDTO(user);
  }

  /**
   * Get user profile
   * 
   * @return user info
   */
  @Override
  public UserResponse getProfile() {
    Long userId = Long.parseLong(userUtil.getCurrentUserId());

    log.debug("Retrieving user profile: {}", userId);

    User user = userRepository.findById(userId).orElseThrow(() -> {
      log.error("User not found with ID: {}", userId);
      return new AppException(ErrorCode.USER_NOT_FOUND);
    });

    return userMapper.toDTO(user);
  }

  /**
   * Create a new user
   * 
   * @param request user details
   * @return created user info
   */
  @Override
  @Transactional
  public UserResponse save(CreateUserRequest request) {
    request.setEmail(request.getEmail().toLowerCase());

    log.info("Attempting to save user with email: {}", request.getEmail());

    Optional<User> existUser = userRepository.findByEmail(request.getEmail());

    existUser.ifPresent(userFound -> {
      log.warn("Email already registered: {}", request.getEmail());
      throw new AppException(ErrorCode.EMAIL_ALREADY_REGISTERED);
    });

    User savedUser = userRepository.save(userMapper.toEntity(request));

    log.info("User saved successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());

    return userMapper.toDTO(savedUser);
  }

  /**
   * Update user stats
   * 
   * @param request user stats update request
   * @return updated user info
   */
  @Override
  @Transactional
  public void updateUserStats(UpdateUserStatsRequest request) {
    Long id = request.getId();

    log.info("Updating user stats for user ID: {}", id);

    Optional<User> existUser = userRepository.findById(id);

    existUser.ifPresentOrElse(user -> {
      updateStats(user, request.getNewAchievePoint());
      log.info("User stats updated successfully for ID: {}", id);
    }, () -> {
      log.error("User not found with ID: {}", id);
      throw new AppException(ErrorCode.USER_NOT_FOUND);
    });
  }

  /**
   * Hard delete user by ID
   * 
   * @param id user id
   */
  @Override
  @Transactional
  public void hardDelete(Long id) {
    log.info("Attempting to hard delete user with ID: {}", id);

    Optional<User> existUser = userRepository.findById(id);

    existUser.ifPresentOrElse(user -> {
      userRepository.delete(user);
      log.warn("User hard deleted successfully - ID: {}, Email: {}", user.getId(), user.getEmail());
    }, () -> {
      log.error("User not found with ID: {}", id);
      throw new AppException(ErrorCode.USER_NOT_FOUND);
    });
  }

  /**
   * Update user profile
   * 
   * @param request update fields
   * @return new user info
   */
  @Override
  @Transactional
  public UserResponse updateProfile(UpdateProfileRequest request) {
    Long userId = Long.parseLong(userUtil.getCurrentUserId());

    log.info("Attempting to update user profile with id: {}", userId);

    User existUser = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("User not found for update with id: {}", userId);
          return new AppException(ErrorCode.USER_NOT_FOUND);
        });

    User updatedUser = userMapper.update(request, existUser);

    log.info("User updated successfully with ID: {}", updatedUser.getId());

    return userMapper.toDTO(updatedUser);
  }

  // ======================== UTILS OPERATIONS ========================
  /**
   * Update user stats
   * 
   * @param user
   * @param newPoint
   */
  private void updateStats(User user, double newPoint) {
    double oldPoint = user.getTotalSubmissionPoint();
    user.setTotalSubmissionPoint(oldPoint + newPoint);

    AccountAchievement newAchievement = getAchievement(user.getTotalSubmissionPoint());
    user.setAchievement(newAchievement);
    
    userRepository.save(user);
  }

  /**
   * Get user achievement level based on points
   * 
   * @param point
   * @return
   */
  private AccountAchievement getAchievement(double point) {
    if (point < 1000) {
      return AccountAchievement.BEGINNER;
    } else if (point < 5000) {
      return AccountAchievement.INTERMEDIATE;
    } else {
      return AccountAchievement.EXPERT;
    }
  }
}