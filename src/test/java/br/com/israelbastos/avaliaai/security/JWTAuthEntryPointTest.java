package br.com.israelbastos.avaliaai.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import javax.servlet.http.HttpServletResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JWTAuthEntryPointTest {

    @Test
    @DisplayName("commence sends unauthorized error")
    public void commenceAndSendsUnauthorizedError() throws Exception {
        JWTAuthEntryPoint entryPoint = new JWTAuthEntryPoint();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = new AuthenticationException("Test exception") {};

        entryPoint.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("Test exception", response.getErrorMessage());
    }
}
