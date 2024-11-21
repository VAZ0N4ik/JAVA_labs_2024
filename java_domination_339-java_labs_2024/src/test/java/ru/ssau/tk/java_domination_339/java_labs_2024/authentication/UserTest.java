package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testBuilder() {
        User user = User.builder()
                .id(1L)
                .username("testUser")
                .password("password")
                .role(Role.ROLE_USER)
                .build();

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(Role.ROLE_USER, user.getRole());
    }

    @Test
    void testGetAuthorities() {
        User user = User.builder()
                .role(Role.ROLE_ADMIN)
                .build();

        var authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void testUserDetails() {
        User user = new User();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();

        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getRole());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1L, "testUser", "password", Role.ROLE_USER);

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(Role.ROLE_USER, user.getRole());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        user.setId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);

        assertEquals(1L, user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals(Role.ROLE_USER, user.getRole());
    }
}