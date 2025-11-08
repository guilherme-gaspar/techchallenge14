package com.fiap.techchallenge14.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken_ShouldCreateValidToken() {
        String token = jwtService.generateToken(1L, "user123");

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));

        String username = jwtService.extractUsername(token);
        Long userId = jwtService.extractUserId(token);

        assertEquals("user123", username);
        assertEquals(1L, userId);
    }

    @Test
    void isValid_ShouldReturnFalseForInvalidSignature() {
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.fake.payload.signature";

        assertFalse(jwtService.isValid(fakeToken));
    }

    @Test
    void extractUsername_ShouldReturnExpectedValue() {
        String token = jwtService.generateToken(123L, "guilherme");
        String username = jwtService.extractUsername(token);

        assertEquals("guilherme", username);
    }

    @Test
    void extractUserId_ShouldReturnExpectedValue() {
        String token = jwtService.generateToken(99L, "test_user");
        Long id = jwtService.extractUserId(token);

        assertEquals(99L, id);
    }

    @Test
    void extractUsername_ShouldThrowForInvalidToken() {
        String invalid = "invalid.jwt.token";

        assertThrows(Exception.class, () -> jwtService.extractUsername(invalid));
    }
}
