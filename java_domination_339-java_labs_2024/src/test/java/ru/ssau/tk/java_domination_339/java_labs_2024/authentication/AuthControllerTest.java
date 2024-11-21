package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_ShouldReturnJwtResponse() {
        SignUpRequest request = new SignUpRequest();
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("test-token");
        when(authenticationService.signUp(any(SignUpRequest.class))).thenReturn(expectedResponse);

        JwtAuthenticationResponse actualResponse = authController.signUp(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getToken(), actualResponse.getToken());
        verify(authenticationService, times(1)).signUp(request);
    }

    @Test
    void signIn_ShouldReturnJwtResponse() {
        SignInRequest request = new SignInRequest();
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("test-token");
        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(expectedResponse);

        JwtAuthenticationResponse actualResponse = authController.signIn(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getToken(), actualResponse.getToken());
        verify(authenticationService, times(1)).signIn(request);
    }
}