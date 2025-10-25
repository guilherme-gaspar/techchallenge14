package com.fiap.techchallenge14.user.mapper;

import com.fiap.techchallenge14.user.dto.PasswordChangeRequestDTO;
import com.fiap.techchallenge14.user.model.PasswordChange;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PasswordMapper {

    PasswordChange toEntity(PasswordChangeRequestDTO passwordChangeRequestDTO);
}
