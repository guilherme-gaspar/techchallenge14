package com.fiap.techchallenge14.user.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String address,
        String login,
        LocalDateTime createdAt,
        LocalDateTime lastUpdatedAt,
        LocalDateTime lastLoginAt,
        Boolean active,
        String roleName
) {
}
