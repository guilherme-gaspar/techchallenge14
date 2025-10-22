package com.fiap.techchallenge14.user.service;

import com.fiap.techchallenge14.exception.UserException;
import com.fiap.techchallenge14.role.model.Role;
import com.fiap.techchallenge14.role.model.RoleType;
import com.fiap.techchallenge14.role.repository.RoleRepository;
import com.fiap.techchallenge14.user.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.user.mapper.UserMapperImpl;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    void setup() {
        UserMapperImpl userMapper = new UserMapperImpl();
        userService = new UserService(userRepository, roleRepository, userMapper);

        role = new Role();
        role.setId(1L);
        role.setName(RoleType.CLIENT);

        user = new User();
        user.setId(1L);
        user.setName("teste");
        user.setEmail("teste@email.com");
        user.setLogin("login");
        user.setActive(true);
        user.setRole(role);
    }

    @Test
    void save_ShouldCreateUserSuccessfully() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO("teste", "teste@email.com", "123", "address", "login", 1L);

        when(roleRepository.getReferenceById(1L)).thenReturn(role);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO result = userService.save(dto);

        assertEquals("teste", result.name());
        assertEquals("teste@email.com", result.email());
        assertEquals(RoleType.CLIENT.name(), result.roleName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findAll_ShouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals(user.getEmail(), result.getFirst().email());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findUserByName_ShouldReturnFilteredUsers() {
        when(userRepository.findByNameContainingIgnoreCase("teste")).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.findUserByName("teste");

        assertEquals(1, result.size());
        assertEquals("teste", result.getFirst().name());
        verify(userRepository).findByNameContainingIgnoreCase("teste");
    }

    @Test
    void findUserByName_ShouldThrowException_WhenNotFound() {
        when(userRepository.findByNameContainingIgnoreCase("NotFound")).thenReturn(List.of());

        assertThrows(UserException.class, () -> userService.findUserByName("NotFound"));
    }

    @Test
    void update_ShouldUpdateUserSuccessfully() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("teste Updated", "updated@mail.com", "address", "login", 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.getReferenceById(1L)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDTO result = userService.update(1L, dto);

        assertNotNull(result);
        assertEquals("teste Updated", result.name());
        assertEquals("updated@mail.com", result.email());
        assertEquals("login", result.login());
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("Test", "t@test.com", "address", "login", 1L);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> userService.update(99L, dto));
    }

    @Test
    void update_ShouldThrowException_WhenEmailAlreadyInUse() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "existing@email.com",
                "newLogin",
                "address",
                1L
        );

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setEmail("existing@email.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(otherUser));

        UserException exception = assertThrows(UserException.class, () -> userService.update(1L, dto));
        assertEquals("E-mail já está em uso", exception.getMessage());
    }

    @Test
    void update_ShouldThrowException_WhenLoginAlreadyInUse() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "new@mail.com",
                "existingLogin",
                "address",
                1L
        );

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setLogin("existingLogin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.of(otherUser));

        UserException exception = assertThrows(UserException.class, () -> userService.update(1L, dto));
        assertEquals("Login já está em uso", exception.getMessage());
    }

    @Test
    void update_ShouldAllowSameEmailForSameUser() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "teste@email.com",
                "newLogin",
                "address",
                1L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.empty());
        when(roleRepository.getReferenceById(dto.roleId())).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDTO result = userService.update(1L, dto);

        assertEquals("teste Updated", result.name());
        assertEquals("teste@email.com", result.email());
    }

    @Test
    void update_ShouldAllowSameLoginForSameUser() {
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO(
                "teste Updated",
                "new@mail.com",
                "address",
                "login",
                1L
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(userRepository.findByLogin(dto.login())).thenReturn(Optional.of(user));
        when(roleRepository.getReferenceById(dto.roleId())).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);

        UserResponseDTO result = userService.update(1L, dto);

        assertEquals("teste Updated", result.name());
        assertEquals("login", result.login());
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

        assertThrows(UserException.class, () -> userService.changePassword(50L, "newpass"));
    }
}
