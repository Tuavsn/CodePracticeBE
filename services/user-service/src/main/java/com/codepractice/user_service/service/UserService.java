package com.codepractice.user_service.service;

import java.util.List;

import com.codepractice.user_service.model.dto.request.UserRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;

public interface UserService {
  public UserResponse save(UserRequest user);

  public UserResponse update(UserRequest user);

  public UserResponse updateProfile(UserRequest user);

  public void hardDelete(Long id);

  public List<UserResponse> getAll();

  public UserResponse getById(Long id);

  public UserResponse getProfile();
}
