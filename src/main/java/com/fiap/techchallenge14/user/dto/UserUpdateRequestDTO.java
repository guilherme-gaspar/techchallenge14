package com.fiap.techchallenge14.user.dto;

import com.fiap.techchallenge14.role.validation.RoleExists;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDTO(
        @NotBlank(message = "O nome é obrigatório")
        @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
        String name,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "O endereco é obrigatório")
        @Size(min = 3, max = 100, message = "O endereco deve ter entre 3 e 100 caracteres")
        Long address,

        @NotNull
        @RoleExists
        Long roleId
) {
}
