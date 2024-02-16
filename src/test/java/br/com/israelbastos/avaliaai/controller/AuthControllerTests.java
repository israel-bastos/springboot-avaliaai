package br.com.israelbastos.avaliaai.controller;

import br.com.israelbastos.avaliaai.dto.RegisterDTO;
import br.com.israelbastos.avaliaai.entity.Role;
import br.com.israelbastos.avaliaai.entity.UserEntity;
import br.com.israelbastos.avaliaai.repository.RoleRepository;
import br.com.israelbastos.avaliaai.repository.UserRepository;
import br.com.israelbastos.avaliaai.security.JWTGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JWTGenerator jwtGenerator;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("should return 200 and token when credentials are valid")
    public void shouldReturn200AndTokenWhenCredentialsAreValid() throws Exception {
        String validUsername = "root";
        String validPassword = "root";

        GrantedAuthority authority = new SimpleGrantedAuthority("USER");
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);

        User principal = new User(validUsername, validPassword, authorities);

        Authentication authentication = Mockito.mock(Authentication.class);
        doReturn(true).when(authentication).isAuthenticated();
        doReturn(principal).when(authentication).getPrincipal();
        doReturn(authorities).when(authentication).getAuthorities();
        doReturn(validUsername).when(authentication).getName();

        doReturn(authentication).when(authenticationManager).authenticate(Mockito.any(Authentication.class));

        String mockAccessToken = "mockJwtToken";
        String mockTokenType = "Bearer ";
        when(jwtGenerator.generateToken(authentication)).thenReturn(mockAccessToken);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + validUsername + "\", \"password\":\"" + validPassword + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(mockAccessToken))
                .andExpect(jsonPath("$.tokenType").value(mockTokenType));
    }

    @Test
    @DisplayName("should return unauthorized for invalid credentials")
    public void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
        String invalidUsername = "wrong";
        String invalidPassword = "credentials";

        when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + invalidUsername + "\", \"password\":\"" + invalidPassword + "\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return success when register new user")
    public void shouldRegisterNewUserShouldReturnSuccess() throws Exception {
        RegisterDTO newUser = new RegisterDTO("newUser", "root");

        when(userRepository.existsByUsername(newUser.username())).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
        when(passwordEncoder.encode(newUser.password())).thenReturn("encodedPassword");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered success!"));

        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("should return bad request exception when username already exist")
    public void shouldReturnBadRequestExceptionWhenUsernameAlreadyExist() throws Exception {
        RegisterDTO existingUser = new RegisterDTO("existingUser", "wrongPassword");

        when(userRepository.existsByUsername(existingUser.username())).thenReturn(true);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(existingUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is taken!"));
    }

    @Test
    @DisplayName("should return bad request exception when user role do not exist")
    public void shouldReturnBadRequestExceptionWhenUserRoleNotExist() throws Exception {
        RegisterDTO newUser = new RegisterDTO("newUser", "root");

        when(userRepository.existsByUsername(newUser.username())).thenReturn(false);
        when(passwordEncoder.encode(newUser.password())).thenReturn("encodedPassword");
        when(roleRepository.findByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User role not found!"));
    }
}
