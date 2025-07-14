package com.codepractice.user_service.service;

import java.util.List;

import com.codepractice.user_service.model.dto.request.UserRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.model.entity.User;

public interface UserService {
  public UserResponse save(UserRequest user);

  public UserResponse update(User user);

  public void block(Long id);

  public void hardDelete(Long id);

  public List<UserResponse> getAll();

  public UserResponse getById(Long id);

  public UserResponse getByEmail(String email);

  public UserResponse getByUsername(String username);
}
