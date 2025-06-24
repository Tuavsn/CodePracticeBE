package com.codepractice.user_service.model.entity;

import com.codepractice.common_lib.model.entity.JpaBaseEntity;
import com.codepractice.user_service.enums.AccountAchievement;
import com.codepractice.user_service.enums.AccountGender;
import com.codepractice.user_service.enums.AccountRole;
import com.codepractice.user_service.enums.AccountStatus;
import com.codepractice.user_service.enums.AuthStrategy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends JpaBaseEntity {
  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(columnDefinition = "LONGTEXT")
  private String avatar;

  @Column(columnDefinition = "LONGTEXT")
  private String phone;

  @Column(columnDefinition = "LONGTEXT")
  private String address;

  @Column(columnDefinition = "LONGTEXT")
  private String bio;

  private long totalSubmissionPoint;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AccountRole role;

  @Enumerated(EnumType.STRING)
  private AccountGender gender;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AccountAchievement achievement;

  @Enumerated(EnumType.STRING)
  private AuthStrategy authStrategy;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AccountStatus status;
}
