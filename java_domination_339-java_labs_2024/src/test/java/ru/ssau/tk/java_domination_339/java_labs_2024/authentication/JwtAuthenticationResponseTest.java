package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationResponseTest {

    @Test
    void testNoArgsConstructor() {
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        assertNull(response.getToken());
    }

    @Test
    void testAllArgsConstructor() {
        String token = "test.jwt.token";
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);
        assertEquals(token, response.getToken());
    }

    @Test
    void testBuilder() {
        String token = "test.jwt.token";
        JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
                .token(token)
                .build();
        assertEquals(token, response.getToken());
    }

    @Test
    void testSetterAndGetter() {
        String token = "test.jwt.token";
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        response.setToken(token);
        assertEquals(token, response.getToken());
    }

    @Test
    void testEqualsAndHashCode() {
        String token = "test.jwt.token";
        JwtAuthenticationResponse response1 = new JwtAuthenticationResponse(token);
        JwtAuthenticationResponse response2 = new JwtAuthenticationResponse(token);
        JwtAuthenticationResponse response3 = new JwtAuthenticationResponse("different.token");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
    }
}