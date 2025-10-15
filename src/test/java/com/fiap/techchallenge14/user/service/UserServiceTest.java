package com.fiap.techchallenge14.user.service;

import com.fiap.techchallenge14.exception.UserException;
import com.fiap.techchallenge14.role.model.Role;
import com.fiap.techchallenge14.role.model.RoleType;
import com.fiap.techchallenge14.role.repository.RoleRepository;
import com.fiap.techchallenge14.user.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.user.mapper.UserMapper;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        role = new Role();
        role.setId(1L);
        role.setName(RoleType.CLIENT);

        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setActive(true);
        user.setRole(role);

        responseDTO = new UserResponseDTO(
                1L,
                "John",
                "john@example.com",
                "ADMIN",
                null,
                null,
                null,
                true,
                RoleType.CLIENT.name()
        );
    }

    @Test
    void save_ShouldCreateUserSuccessfully() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("John", "john@example.com", "123", "address", 1L);

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(roleRepository.getReferenceById(1L)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.save(dto);

        assertEquals("John", result.name());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toEntity(dto);
    }

    @Test
    void findAll_ShouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        List<UserResponseDTO> result = userService.findAll();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findUserByName_ShouldReturnFilteredUsers() {
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        List<UserResponseDTO> result = userService.findUserByName("John");

        assertEquals(1, result.size());
        verify(userRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    void findUserByName_ShouldThrowException_WhenNotFound() {
        when(userRepository.findByNameContainingIgnoreCase("NotFound")).thenReturn(List.of());

        assertThrows(UserException.class, () -> userService.findUserByName("NotFound"));
    }

    @Test
    void update_ShouldUpdateUserSuccessfully() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("John Updated", "updated@mail.com", "address", 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.getReferenceById(1L)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = userService.update(1L, dto);

        assertNotNull(result);
        verify(userMapper).updateEntityFromDto(dto, user);
        verify(userRepository).save(user);
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("Test", "t@test.com", "address", 1L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.update(99L, dto));
    }

    @Test
    void delete_ShouldSoftDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        assertFalse(user.getActive());
        verify(userRepository).save(user);
    }

    @Test
    void delete_ShouldNotSave_WhenAlreadyInactive() {
        user.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository, never()).save(user);
    }

    @Test
    void delete_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.delete(10L));
    }

    @Test
    void changePassword_ShouldUpdatePassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.changePassword(1L, "newpass");

        assertEquals("newpass", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findById(50L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.changePassword(50L, "xpto"));
    }
}
