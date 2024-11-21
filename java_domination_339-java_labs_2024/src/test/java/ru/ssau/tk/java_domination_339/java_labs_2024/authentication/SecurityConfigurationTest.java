package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SecurityConfigurationTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(userService.userDetailsService()).thenReturn(username -> {
            UserDetails mockUser = mock(UserDetails.class);
            when(mockUser.getUsername()).thenReturn(username);
            return mockUser;
        });
    }

    @Test
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfiguration.passwordEncoder();
        assertNotNull(encoder);
        String password = "testPassword";
        String encodedPassword = encoder.encode(password);
        assertTrue(encoder.matches(password, encodedPassword));
    }

    @Test
    void authenticationProvider_ShouldConfigureCorrectly() {
        AuthenticationProvider provider = securityConfiguration.authenticationProvider();
        assertNotNull(provider);
        assertInstanceOf(DaoAuthenticationProvider.class, provider);
    }

    @Test
    void authenticationManager_ShouldReturnManager() throws Exception {
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);
        AuthenticationManager manager = securityConfiguration.authenticationManager(config);
        assertNotNull(manager);
        assertInstanceOf(ProviderManager.class, manager);
    }
}