package com.fiap.techchallenge14.user.mapper;

import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "role.name", target = "roleName")
    UserResponseDTO toResponseDTO(User user);
}
