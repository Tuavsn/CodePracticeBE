package com.codepractice.user_service.service;

import java.util.List;

import com.codepractice.user_service.model.entity.User;

public interface UserService {
  public User save(User user);

  public User update(User user);

  public void delete(String id);

  public void hardDelete(String id);

  public List<User> getAll();

  public User getById(String id);

  public User getByEmail(String email);

  public User getByUsername(String username);
}
