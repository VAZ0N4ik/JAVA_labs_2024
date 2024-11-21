package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SignInRequestTest {

    private Validator validator;
    private SignInRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        request = new SignInRequest();
    }

    @Test
    void testValidRequest() {
        request.setUsername("validUser");
        request.setPassword("validPassword123");

        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testUsernameTooShort() {
        request.setUsername("user");
        request.setPassword("validPassword123");

        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Имя пользователя должно содержать от 5 до 50 символов",
                violations.iterator().next().getMessage());
    }

    @Test
    void testUsernameTooLong() {
        request.setUsername("a".repeat(51));
        request.setPassword("validPassword123");

        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Имя пользователя должно содержать от 5 до 50 символов",
                violations.iterator().next().getMessage());
    }

    @Test
    void testUsernameBlank() {
        request.setUsername("   ");
        request.setPassword("validPassword123");

        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Имя пользователя не может быть пустыми")));
    }

    @Test
    void testPasswordTooShort() {
        request.setUsername("validUser");
        request.setPassword("short");

        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Длина пароля должна быть от 8 до 255 символов",
                violations.iterator().next().getMessage());
    }

    @Test
    void testPasswordTooLong() {
        request.setUsername("validUser");
        request.setPassword("a".repeat(256));

        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);
        assertEquals(1, violations.size());
        assertEquals("Длина пароля должна быть от 8 до 255 символов",
                violations.iterator().next().getMessage());
    }

    @Test
    void testPasswordBlank() {
        request.setUsername("validUser");
        request.setPassword("   ");

        Set<ConstraintViolation<SignInRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Пароль не может быть пустыми")));
    }

    @Test
    void testEqualsAndHashCode() {
        SignInRequest request1 = new SignInRequest();
        SignInRequest request2 = new SignInRequest();

        request1.setUsername("user");
        request1.setPassword("password");
        request2.setUsername("user");
        request2.setPassword("password");

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }
}