package com.codepractice.common_lib.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.NullValuePropertyMappingStrategy;

@MapperConfig(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CentralMapperConfig {
}
