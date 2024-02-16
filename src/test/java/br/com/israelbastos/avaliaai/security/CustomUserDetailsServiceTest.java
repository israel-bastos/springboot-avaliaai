package br.com.israelbastos.avaliaai.security;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import br.com.israelbastos.avaliaai.entity.Role;
import br.com.israelbastos.avaliaai.entity.UserEntity;
import br.com.israelbastos.avaliaai.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("load user by username and user exists returns UserDetails")
    public void loadUserByUsernameAndUserExistsReturnsUserDetails() {
        // Setup
        UserEntity foundUser = new UserEntity();
        foundUser.setUsername("user");
        foundUser.setPassword("password");
        foundUser.setRoles(List.of(new Role("USER")));
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(foundUser));

        // Execute
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("user");

        // Verify
        assertEquals("user", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("USER")));

        // Cleanup
        verify(userRepository).findByUsername("user");
    }

    @Test
    @DisplayName("load user by username and user not found throws UsernameNotFoundException")
    public void loadUserByUsernameAndUserNotFoundThrowsUsernameNotFoundException() {
        // Setup
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("nonexistent");
        });
    }
}
