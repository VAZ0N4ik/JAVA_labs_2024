package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void testSave() {
        when(repository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.save(testUser);

        assertEquals(testUser, savedUser);
        verify(repository).save(testUser);
    }

    @Test
    void testCreateSuccess() {
        when(repository.existsByUsername(testUser.getUsername())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(testUser);

        User createdUser = userService.create(testUser);

        assertEquals(testUser, createdUser);
        verify(repository).existsByUsername(testUser.getUsername());
        verify(repository).save(testUser);
    }

    @Test
    void testCreateWithExistingUsername() {
        when(repository.existsByUsername(testUser.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(testUser));
        verify(repository).existsByUsername(testUser.getUsername());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void testGetByUsernameSuccess() {
        when(repository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        User foundUser = userService.getByUsername(testUser.getUsername());

        assertEquals(testUser, foundUser);
        verify(repository).findByUsername(testUser.getUsername());
    }

    @Test
    void testGetByUsernameNotFound() {
        when(repository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.getByUsername(testUser.getUsername()));
        verify(repository).findByUsername(testUser.getUsername());
    }

    @Test
    void testUserDetailsService() {
        when(repository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        var userDetailsService = userService.userDetailsService();
        var userDetails = userDetailsService.loadUserByUsername(testUser.getUsername());

        assertEquals(testUser, userDetails);
    }

    @Test
    void testGetCurrentUser() {
        var authentication = new UsernamePasswordAuthenticationToken(testUser.getUsername(), null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(repository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        User currentUser = userService.getCurrentUser();

        assertEquals(testUser, currentUser);
        verify(repository).findByUsername(testUser.getUsername());
    }

    @Test
    void testGetAdmin() {
        var authentication = new UsernamePasswordAuthenticationToken(testUser.getUsername(), null);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(repository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));
        when(repository.save(any(User.class))).thenReturn(testUser);

        userService.getAdmin();

        assertEquals(Role.ROLE_ADMIN, testUser.getRole());
        verify(repository).save(testUser);
    }
}