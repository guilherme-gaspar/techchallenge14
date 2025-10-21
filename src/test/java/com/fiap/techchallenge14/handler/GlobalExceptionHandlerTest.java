package com.fiap.techchallenge14.handler;

import com.fiap.techchallenge14.exception.UserException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleRuntime_ShouldReturnNotFound() {
        RuntimeException ex = new RuntimeException("Erro de runtime");

        ResponseEntity<String> response = handler.handleRuntime(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Erro de runtime", response.getBody());
    }

    @Test
    void handleUserException_ShouldReturnBadRequest() {
        UserException ex = new UserException("Erro de usuário");

        ResponseEntity<String> response = handler.handleUserException(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Erro de usuário", response.getBody());
    }

    @Test
    void handleValidation_ShouldReturnFieldErrors() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError error1 = new FieldError("user", "name", "Nome obrigatório");
        FieldError error2 = new FieldError("user", "email", "Email inválido");

        when(bindingResult.getAllErrors()).thenReturn(List.of(error1, error2));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidation(ex);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Nome obrigatório", response.getBody().get("name"));
        assertEquals("Email inválido", response.getBody().get("email"));
    }
}
