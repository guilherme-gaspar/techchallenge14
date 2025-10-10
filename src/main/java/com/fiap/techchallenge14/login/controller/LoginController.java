package com.fiap.techchallenge14.login.controller;

import com.fiap.techchallenge14.login.dto.LoginRequestDTO;
import com.fiap.techchallenge14.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
        String token = loginService.login(loginRequest);
        return ResponseEntity.ok(token);
    }
}

