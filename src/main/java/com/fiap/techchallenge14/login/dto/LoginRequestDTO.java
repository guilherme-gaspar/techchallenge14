package com.fiap.techchallenge14.login.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}