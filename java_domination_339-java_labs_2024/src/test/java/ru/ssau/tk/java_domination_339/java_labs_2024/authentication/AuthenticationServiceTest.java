package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_ShouldCreateUserAndReturnToken() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("testUser");
        request.setPassword("password123");

        String encodedPassword = "encodedPassword";
        String expectedToken = "jwt-token";

        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedToken);

        JwtAuthenticationResponse response = authenticationService.signUp(request);

        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());

        verify(passwordEncoder).encode(request.getPassword());
        verify(userService).create(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void signIn_ShouldAuthenticateAndReturnToken() {
        // Arrange
        SignInRequest request = new SignInRequest();
        request.setUsername("testUser");
        request.setPassword("password123");

        User mockUser = User.builder()
                .username("testUser")
                .password("encodedPassword")
                .role(Role.ROLE_USER)
                .build();

        String expectedToken = "jwt-token";

        when(userService.userDetailsService()).thenReturn(username -> mockUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedToken);

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        verify(jwtService).generateToken(mockUser);
    }
}