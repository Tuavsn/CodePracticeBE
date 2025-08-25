package com.codepractice.user_service.service;

import java.util.List;

import com.codepractice.user_service.model.dto.internal.CreateUserRequest;
import com.codepractice.user_service.model.dto.internal.UpdateUserStatsRequest;
import com.codepractice.user_service.model.dto.request.UpdateProfileRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;

public interface UserService {
  public List<UserResponse> getAll();

  public UserResponse getById(Long id);

  public UserResponse getProfile();

  public UserResponse save(CreateUserRequest request);

  public UserResponse updateProfile(UpdateProfileRequest request);

  public void updateUserStats(UpdateUserStatsRequest request);

  public void hardDelete(Long id);
}
