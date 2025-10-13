package com.fiap.techchallenge14.role.validation;

import com.fiap.techchallenge14.role.repository.RoleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleExistsValidator implements ConstraintValidator<RoleExists, Long> {

    private final RoleRepository roleRepository;

    @Override
    public boolean isValid(Long roleId, ConstraintValidatorContext context) {
        if (roleId == null) {
            return false;
        }
        return roleRepository.existsById(roleId);
    }
}
