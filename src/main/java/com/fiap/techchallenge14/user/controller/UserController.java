package com.fiap.techchallenge14.user.controller;

import com.fiap.techchallenge14.user.dto.PasswordChangeRequestDTO;
import com.fiap.techchallenge14.user.dto.UserCreateRequestDTO;
import com.fiap.techchallenge14.user.dto.UserResponseDTO;
import com.fiap.techchallenge14.user.dto.UserUpdateRequestDTO;
import com.fiap.techchallenge14.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.save(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO userRequestDTO) {

        UserResponseDTO updatedUser = userService.update(id, userRequestDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers(@RequestParam(required = false) String name) {
        List<UserResponseDTO> users = (name != null && !name.trim().isEmpty())
                ? userService.findUserByName(name)
                : userService.findAll();

        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody PasswordChangeRequestDTO request
    ) {
        userService.changePassword(id, request.newPassword());
        return ResponseEntity.noContent().build();
    }
}
