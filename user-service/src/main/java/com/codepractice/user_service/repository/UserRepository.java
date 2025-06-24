package com.codepractice.user_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codepractice.user_service.enums.AccountStatus;
import com.codepractice.user_service.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  List<User> findAllByStatus(AccountStatus status);
}