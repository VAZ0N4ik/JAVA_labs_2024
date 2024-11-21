package ru.ssau.tk.java_domination_339.java_labs_2024.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;
    private User customUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = mock(UserDetails.class);
        customUser = mock(User.class);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(customUser.getUsername()).thenReturn("testUser");
        when(customUser.getId()).thenReturn(1L);
        when(customUser.getRole()).thenReturn(Role.ROLE_USER);
    }

    @Test
    void generateToken_WithUserDetails_ShouldReturnValidToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testUser", jwtService.extractUserName(token));
    }

    @Test
    void generateToken_WithCustomUser_ShouldIncludeCustomClaims() {
        String token = jwtService.generateToken(customUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testUser", jwtService.extractUserName(token));
    }

    @Test
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        String token = jwtService.generateToken(userDetails);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void isTokenValid_WithInvalidUsername_ShouldReturnFalse() {
        String token = jwtService.generateToken(userDetails);
        UserDetails differentUser = mock(UserDetails.class);
        when(differentUser.getUsername()).thenReturn("differentUser");

        assertFalse(jwtService.isTokenValid(token, differentUser));
    }

    @Test
    void extractUserName_WithValidToken_ShouldReturnUsername() {
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUserName(token);

        assertEquals("testUser", extractedUsername);
    }

    @Test
    void generateToken_ShouldCreateTokenWithExpiration() {
        String token = jwtService.generateToken(userDetails);
        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode("MEGASUPADUPAKEY23891471UNHACKABLE1251521526218BEBRA12232411")))
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertNotNull(claims.getExpiration());
        assertTrue(claims.getExpiration().after(new Date()));
    }
}