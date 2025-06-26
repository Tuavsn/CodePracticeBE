package com.codepractice.user_service.model.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.codepractice.user_service.enums.AccountAchievement;
import com.codepractice.user_service.enums.AccountGender;
import com.codepractice.user_service.enums.AccountRole;
import com.codepractice.user_service.enums.AccountStatus;
import com.codepractice.user_service.enums.AuthStrategy;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
