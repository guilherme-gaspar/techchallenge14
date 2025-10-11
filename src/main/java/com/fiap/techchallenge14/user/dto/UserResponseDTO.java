package com.fiap.techchallenge14.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private LocalDateTime lastLoginAt;
    private Boolean active;
}
