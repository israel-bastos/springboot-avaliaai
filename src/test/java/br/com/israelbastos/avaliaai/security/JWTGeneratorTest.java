package br.com.israelbastos.avaliaai.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class JWTGeneratorTest {

    private final JWTGenerator jwtGenerator = new JWTGenerator();

    @Test
    @DisplayName("generate token and returns an valid token")
    void generateTokenAndReturnsAnValidToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "user",
                null,
                Collections.emptyList());

        String token = jwtGenerator.generateToken(authentication);
        assertNotNull(token);
    }

    @Test
    @DisplayName("get username from JWT and extracts the correct username")
    void getUsernameFromJWTAmdExtractsTheCorrectUsername() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
        String token = jwtGenerator.generateToken(authentication);

        String username = jwtGenerator.getUsernameFromJWT(token);
        assertEquals("user", username);
    }

    @Test
    @DisplayName("validate token and valid token must returns true")
    void validateTokenAndValidTokenMustReturnsTrue() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
        String token = jwtGenerator.generateToken(authentication);

        assertTrue(jwtGenerator.validateToken(token));
    }

    @Test
    @DisplayName("validate token and invalid token must throws AuthenticationCredentialsNotFoundException")
    void validateTokenAndInvalidTokenMustThrowsAuthenticationCredentialsNotFoundException() {
        String invalidToken = "invalid.token";

        assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> jwtGenerator.validateToken(invalidToken));
    }
}
