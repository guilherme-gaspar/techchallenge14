package com.fiap.techchallenge14.login.service;

import com.fiap.techchallenge14.exception.LoginException;
import com.fiap.techchallenge14.login.dto.LoginRequestDTO;
import com.fiap.techchallenge14.login.dto.LoginResponseDTO;
import com.fiap.techchallenge14.login.storage.TokenStorage;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        User user = authenticate(loginRequest.login(), loginRequest.password());

        updateLastLogin(user);

        String token = generateToken(user);

        return new LoginResponseDTO(token);
    }

    private User authenticate(String username, String password) {
        return userRepository.findByLoginAndPassword(username, password)
                .filter(User::getActive)
                .orElseThrow(() -> new LoginException("Login ou senha inválidos"));
    }

    private void updateLastLogin(User user) {
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("Usuário {} fez login em {}", user.getName(), user.getLastLoginAt());
    }

    private String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        TokenStorage.saveToken(token, user.getId());
        return token;
    }
}
