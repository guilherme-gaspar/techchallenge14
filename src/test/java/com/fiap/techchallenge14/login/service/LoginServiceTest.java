package com.fiap.techchallenge14.login.service;

import com.fiap.techchallenge14.exception.LoginException;
import com.fiap.techchallenge14.login.dto.LoginRequestDTO;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Guilherme");
        user.setLogin("guilherme");
        user.setPassword("123");
        user.setActive(true);
    }

    @Test
    void login_ShouldThrowException_WhenCredentialsInvalid() {
        // Arrange
        when(userRepository.findByLoginAndPassword("wrong", "invalid"))
                .thenReturn(Optional.empty());

        // Act & Assert
        LoginException ex = assertThrows(LoginException.class,
                () -> loginService.login(new LoginRequestDTO("wrong", "invalid")));

        assertEquals("Login ou senha inválidos", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldThrowException_WhenUserInactive() {
        // Arrange
        user.setActive(false);
        when(userRepository.findByLoginAndPassword("guilherme", "123"))
                .thenReturn(Optional.of(user));

        // Act & Assert
        LoginException ex = assertThrows(LoginException.class,
                () -> loginService.login(new LoginRequestDTO("guilherme", "123")));

        assertEquals("Login ou senha inválidos", ex.getMessage());
        verify(userRepository, never()).save(any());
    }
}
