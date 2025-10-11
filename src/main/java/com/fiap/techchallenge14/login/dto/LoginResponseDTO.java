package com.fiap.techchallenge14.login.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponseDTO {
    private String token;
}