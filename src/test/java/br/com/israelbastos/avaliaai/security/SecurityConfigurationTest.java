package br.com.israelbastos.avaliaai.security;

import br.com.israelbastos.avaliaai.dto.RegisterDTO;
import br.com.israelbastos.avaliaai.entity.Role;
import br.com.israelbastos.avaliaai.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
    }

    @Test
    @DisplayName("should access swagger ui then allowed without authentication")
    public void shouldAllowAccessToSwaggerUIWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should access auth endpoints then allowed without authentication")
    public void shouldAllowAccessToAuthEndpointsWithoutAuthentication() throws Exception {
        String validUsername = "root";
        String validPassword = "root";

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + validUsername + "\", \"password\":\"" + validPassword + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("should protected endpoint when access without authentication then denied")
    public void shouldDenyAccessToProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/"))
                .andExpect(status().isUnauthorized());
    }
}
