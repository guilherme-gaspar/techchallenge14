package com.fiap.techchallenge14.user.mapper;

import com.fiap.techchallenge14.user.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(source = "role.name", target = "roleName")
    UserResponseDTO toResponseDTO(User user);

    User toEntity(UserCreateRequestDTO dto);

    void updateEntityFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);
}
