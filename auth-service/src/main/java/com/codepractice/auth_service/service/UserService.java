package com.codepractice.auth_service.service;

import com.codepractice.auth_service.model.entity.User;

public interface UserService {
    public User create(User user);

    public User update(User user);

    public User getById(Long id);

    public User getByEmail(String email);

    public User getByUsername(String username);
}
