package com.fiap.techchallenge14.user.controller;

import com.fiap.techchallenge14.user.dto.PasswordChangeRequestDTO;
import com.fiap.techchallenge14.user.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponseDTO userResponse;

    @BeforeEach
    void setup() {
        userResponse = new UserResponseDTO(
                1L,
                "John Doe",
                "john@example.com",
                "adress",
                null,
                null,
                null,
                true,
                "CLIENT"
        );
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        UserCreateRequestDTO request = new UserCreateRequestDTO(
                "John Doe",
                "john@example.com",
                "123456",
                "address",
                1L
        );

        when(userService.save(request)).thenReturn(userResponse);

        ResponseEntity<UserResponseDTO> response = userController.createUser(request);

        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().name());
        verify(userService, times(1)).save(request);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        UserUpdateRequestDTO request = new UserUpdateRequestDTO(
                "John Updated",
                "updated@example.com",
                "adress",
                2L
        );

        UserResponseDTO updatedResponse = new UserResponseDTO(
                1L,
                "John Updated",
                "updated@example.com",
                "adress",
                null,
                null,
                null,
                true,
                "CLIENT"
        );

        when(userService.update(1L, request)).thenReturn(updatedResponse);

        ResponseEntity<UserResponseDTO> response = userController.updateUser(1L, request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("John Updated", response.getBody().name());
        verify(userService, times(1)).update(1L, request);
    }

    @Test
    void deleteUser_ShouldReturnNoContent() {
        doNothing().when(userService).delete(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(userService, times(1)).delete(1L);
    }

    @Test
    void getUsers_ShouldReturnAllUsers_WhenNameIsNull() {
        List<UserResponseDTO> users = List.of(userResponse);

        when(userService.findAll()).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response = userController.getUsers(null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findAll();
        verify(userService, never()).findUserByName(anyString());
    }

    @Test
    void getUsers_ShouldFilterByName() {
        List<UserResponseDTO> users = List.of(userResponse);

        when(userService.findUserByName("John")).thenReturn(users);

        ResponseEntity<List<UserResponseDTO>> response = userController.getUsers("John");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).findUserByName("John");
        verify(userService, never()).findAll();
    }

    @Test
    void changePassword_ShouldReturnNoContent() {
        PasswordChangeRequestDTO request = new PasswordChangeRequestDTO("new123");

        doNothing().when(userService).changePassword(1L, "new123");

        ResponseEntity<Void> response = userController.changePassword(1L, request);

        assertEquals(204, response.getStatusCode().value());
        verify(userService, times(1)).changePassword(1L, "new123");
    }
}
