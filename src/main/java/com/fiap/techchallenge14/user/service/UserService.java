package com.fiap.techchallenge14.user.service;

import com.fiap.techchallenge14.exception.EmailAlreadyExistsException;
import com.fiap.techchallenge14.exception.UserException;
import com.fiap.techchallenge14.user.dto.UserRequestDTO;
import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.mapper.UserMapper;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO save(UserRequestDTO dto) {
        validateEmailUniqueness(dto.getEmail());

        User user = buildUserFromDTO(dto);
        User savedUser = userRepository.save(user);
        log.info("User created with ID: {}", savedUser.getId());

        return userMapper.toResponseDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findUserByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);

        if (users.isEmpty()) {
            throw new UserException("No users found with the name: " + name);
        }

        return users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO dto) {
        User user = getUserById(id);

        if (!user.getEmail().equals(dto.getEmail())) {
            validateEmailUniqueness(dto.getEmail());
        }

        updateUserFromDTO(user, dto);
        user.setLastUpdatedAt(LocalDateTime.now());

        User updatedUser = userRepository.save(user);
        log.info("User updated with ID: {}", updatedUser.getId());

        return userMapper.toResponseDTO(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = getUserById(id);

        if (Boolean.FALSE.equals(user.getActive())) {
            log.warn("Attempted to delete already inactive user with ID: {}", id);
            return;
        }

        user.setActive(false);
        userRepository.save(user);

        log.info("User soft-deleted with ID: {}", id);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User not found with ID: " + id));
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private User buildUserFromDTO(UserRequestDTO dto) {
        User user = new User();
        updateUserFromDTO(user, dto);
        return user;
    }

    private void updateUserFromDTO(User user, UserRequestDTO dto) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());
        user.setLastUpdatedAt(LocalDateTime.now());
    }
}
