package com.codepractice.user_service.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codepractice.common_lib.enums.ErrorCode;
import com.codepractice.common_lib.exceptions.AppException;
import com.codepractice.common_lib.utils.UserUtil;
import com.codepractice.user_service.enums.AccountAchievement;
import com.codepractice.user_service.model.dto.request.UserRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.model.entity.User;
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
	private final UserUtil userUtil;

	@Override
	@Transactional
	public UserResponse save(UserRequest request) {
		request.setEmail(request.getEmail().toLowerCase());

		log.info("Attempting to save user with email: {}", request.getEmail());

		Optional<User> existUser = userRepository.findByEmail(request.getEmail());

		existUser.ifPresent(userFound -> {
			log.warn("Email already registered: {}", request.getEmail());
			throw new AppException(ErrorCode.EMAIL_ALREADY_REGISTERED);
		});

		User savedUser = userRepository.save(
				User.builder()
						.id(request.getId())
						.email(request.getEmail())
						.username(request.getUsername())
						.totalSubmissionPoint(0)
						.role(request.getRole())
						.achievement(AccountAchievement.BEGINNER)
						.build());

		log.info("User saved successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());

		return createDTO(savedUser);
	}

	@Override
	@Transactional
	public UserResponse update(UserRequest user) {
		log.info("Attempting to update user with email: {}", user.getEmail());

		User existUser = userRepository.findByEmail(user.getEmail())
				.orElseThrow(() -> {
					log.error("User not found for update with email: {}", user.getEmail());
					return new AppException(ErrorCode.USER_NOT_FOUND);
				});

		User updatedUser = userRepository.save(updateUserDetails(user, existUser));
		log.info("User updated successfully with ID: {}", updatedUser.getId());
		return createDTO(updatedUser);
	}

	@Override
	@Transactional
	public void hardDelete(Long id) {
		log.info("Attempting to hard delete user with ID: {}", id);

		User user = userRepository.findById(id).orElseThrow(() -> {
			log.error("User not found with ID: {}", id);
			return new AppException(ErrorCode.USER_NOT_FOUND);
		});

		userRepository.delete(user);

		log.warn("User hard deleted successfully - ID: {}, Email: {}", user.getId(), user.getEmail());
	}

	@Override
	public List<UserResponse> getAll() {
		log.debug("Retrieving all users");

		List<User> users = userRepository.findAll();

		log.debug("Retrieved {} users", users.size());

		return users.stream().map(user -> createDTO(user)).toList();
	}

	@Override
	public UserResponse getById(Long id) {
		log.debug("Retrieving user by ID: {}", id);

		User user = userRepository.findById(id).orElseThrow(() -> {
			log.error("User not found with ID: {}", id);
			return new AppException(ErrorCode.USER_NOT_FOUND);
		});

		return createDTO(user);
	}

	@Override
	public UserResponse getProfile() {
		Long userId = Long.parseLong(userUtil.getCurrentUserId());

		log.debug("Retrieving user profile: {}", userId);

		User user = userRepository.findById(userId).orElseThrow(() -> {
			log.error("User not found with ID: {}", userId);
			return new AppException(ErrorCode.USER_NOT_FOUND);
		});

		return createDTO(user);
	}

	private User updateUserDetails(UserRequest source, User target) {
		if (source.getAvatar() != null) {
			target.setAvatar(source.getAvatar());
		}

		if (source.getPhone() != null) {
			target.setPhone(source.getPhone());
		}

		if (source.getAddress() != null) {
			target.setAddress(source.getAddress());
		}

		if (source.getBio() != null) {
			target.setBio(source.getBio());
		}

		return target;
	}

	private UserResponse createDTO(User source) {
		if (source != null) {
			return UserResponse.builder()
					.id(source.getId())
					.email(source.getEmail())
					.username(source.getUsername())
					.avatar(source.getAvatar())
					.bio(source.getBio())
					.gender(source.getGender())
					.totalSubmissionPoint(source.getTotalSubmissionPoint())
					.role(source.getRole())
					.achievement(source.getAchievement())
					.build();
		}
		return null;
	}
}