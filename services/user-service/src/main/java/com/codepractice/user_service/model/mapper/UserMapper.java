package com.codepractice.user_service.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.codepractice.common_lib.mapper.CentralMapperConfig;
import com.codepractice.user_service.model.dto.internal.CreateUserRequest;
import com.codepractice.user_service.model.dto.request.UpdateProfileRequest;
import com.codepractice.user_service.model.dto.response.UserResponse;
import com.codepractice.user_service.model.entity.User;

@Mapper(config = CentralMapperConfig.class, componentModel = "spring")
public interface UserMapper {
  /**
   * Maps a UserRequest DTO to a User entity.
   * 
   * @param source
   * @return
   */
  @Mapping(target = "totalSubmissionPoint", constant = "0") // Default value for totalSubmissionPoint
  @Mapping(target = "achievement", expression = "java(com.codepractice.user_service.enums.AccountAchievement.BEGINNER)")
  public User toEntity(CreateUserRequest source);

  /**
   * Maps a User entity to a UserResponse DTO.
   * 
   * @param source
   * @return
   */
  public UserResponse toDTO(User source);

  /**
   * Maps a UserRequest DTO to a User entity.
   * 
   * @param source
   * @param target
   * @return
   */
  public User update(UpdateProfileRequest source, @MappingTarget User target);
}
