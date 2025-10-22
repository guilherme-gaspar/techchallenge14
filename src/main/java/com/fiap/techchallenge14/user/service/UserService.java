package com.fiap.techchallenge14.user.service;

import com.fiap.techchallenge14.exception.UserException;
import com.fiap.techchallenge14.role.model.Role;
import com.fiap.techchallenge14.role.repository.RoleRepository;
import com.fiap.techchallenge14.user.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.user.mapper.UserMapper;
import com.fiap.techchallenge14.user.model.User;
import com.fiap.techchallenge14.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO save(UserCreateRequestDTO dto) {
        User user = userMapper.toEntity(dto);

        Role role = roleRepository.getReferenceById(dto.roleId());
        user.setRole(role);

        User savedUser = userRepository.save(user);
        log.info("Usuário criado com o ID: {}", savedUser.getId());

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
            throw new UserException("Usuário nao encontrado com o nome: " + name);
        }

        return users.stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateRequestDTO dto) {
        User user = getUserById(id);

        userRepository.findByEmail(dto.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new UserException("E-mail já está em uso");
                });

        userRepository.findByLogin(dto.login())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new UserException("Login já está em uso");
                });

        userMapper.updateEntityFromDto(dto, user);

        Role role = roleRepository.getReferenceById(dto.roleId());
        user.setRole(role);

        User updatedUser = userRepository.save(user);
        log.info("Usuário atualizado com o ID: {}", updatedUser.getId());

        return userMapper.toResponseDTO(updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User user = getUserById(id);

        if (Boolean.FALSE.equals(user.getActive())) {
            log.warn("Esse usuário já está inativo, ID: {}", id);
            return;
        }

        user.setActive(false);
        userRepository.save(user);

        log.info("Usuário deletado logicamente com o ID: {}", id);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("Usuário nao encontrado com o ID: " + id));
    }

    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(newPassword);

        userRepository.save(user);
        log.info("Senha atualizada no usuário com o ID: {}", id);
    }
}
