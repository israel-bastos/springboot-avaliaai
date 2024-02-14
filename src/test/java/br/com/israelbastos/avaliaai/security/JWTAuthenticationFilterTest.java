package br.com.israelbastos.avaliaai.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JWTAuthenticationFilterTest {

    @Mock
    private JWTGenerator tokenGenerator;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("should authenticate with valid token")
    void shouldAuthenticateWithValidToken() throws Exception {
        String token = "Bearer validToken";
        request.addHeader("Authorization", token);
        UserDetails mockUserDetails = mock(UserDetails.class);

        when(tokenGenerator.validateToken(anyString())).thenReturn(true);
        when(mockUserDetails.getUsername()).thenReturn("user");
        when(tokenGenerator.getUsernameFromJWT(anyString())).thenReturn("user");
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(mockUserDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("user", authentication.getName());
    }

    @Test
    @DisplayName("should not authenticate with invalid token")
    void shouldNotAuthenticateWithInvalidToken() throws Exception {
        String token = "Bearer invalidToken";
        request.addHeader("Authorization", token);

        when(tokenGenerator.validateToken(anyString())).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication, "Authentication should be null for invalid token");
    }

    @Test
    @DisplayName("should proceed without authentication if token is missing or not added to the request")
    void shouldProceedWithoutAuthenticationIfTokenIsMissingOrNotAddedToTheRequest() throws Exception {
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);
    }
}
