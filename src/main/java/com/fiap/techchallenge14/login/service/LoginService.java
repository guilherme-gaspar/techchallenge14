package com.fiap.techchallenge14.login.service;

import com.fiap.techchallenge14.exception.LoginException;
import com.fiap.techchallenge14.login.dto.LoginRequestDTO;
import com.fiap.techchallenge14.login.storage.TokenStorage;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public String login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword())
                .filter(User::getActive)
                .orElseThrow(() -> new LoginException("Login ou senha inválidos"));

        user.setLastLoginAt(LocalDateTime.now());

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        TokenStorage.saveToken(token, user.getId());
        return token;
    }

}

