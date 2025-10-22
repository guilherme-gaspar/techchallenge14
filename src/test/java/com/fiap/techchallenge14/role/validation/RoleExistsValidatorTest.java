package com.fiap.techchallenge14.role.validation;

import com.fiap.techchallenge14.role.repository.RoleRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleExistsValidatorTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ConstraintValidatorContext context;

    private RoleExistsValidator validator;

    @BeforeEach
    void setup() {
        validator = new RoleExistsValidator(roleRepository);
    }

    @Test
    void isValid_ShouldReturnTrue_WhenRoleExists() {
        // Arrange
        Long roleId = 1L;
        when(roleRepository.existsById(roleId)).thenReturn(true);

        // Act
        boolean result = validator.isValid(roleId, context);

        // Assert
        assertTrue(result);
        verify(roleRepository, times(1)).existsById(roleId);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRoleDoesNotExist() {
        // Arrange
        Long roleId = 2L;
        when(roleRepository.existsById(roleId)).thenReturn(false);

        // Act
        boolean result = validator.isValid(roleId, context);

        // Assert
        assertFalse(result);
        verify(roleRepository, times(1)).existsById(roleId);
    }

    @Test
    void isValid_ShouldReturnFalse_WhenRoleIdIsNull() {
        // Act
        boolean result = validator.isValid(null, context);

        // Assert
        assertFalse(result);
        verifyNoInteractions(roleRepository);
    }
}
