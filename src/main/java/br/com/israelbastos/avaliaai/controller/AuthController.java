package br.com.israelbastos.avaliaai.controller;

import br.com.israelbastos.avaliaai.dto.AuthResponseDTO;
import br.com.israelbastos.avaliaai.dto.LoginDTO;
import br.com.israelbastos.avaliaai.dto.RegisterDTO;
import br.com.israelbastos.avaliaai.entity.Role;
import br.com.israelbastos.avaliaai.entity.UserEntity;
import br.com.israelbastos.avaliaai.repository.RoleRepository;
import br.com.israelbastos.avaliaai.repository.UserRepository;
import br.com.israelbastos.avaliaai.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final JWTGenerator jwtGenerator;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          JWTGenerator jwtGenerator) {

        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("auth/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO dto) {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.username(),
                            dto.password()));


        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(AuthResponseDTO.forSuccess(token), HttpStatus.OK);

        } catch (BadCredentialsException bce) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponseDTO.forError("Invalid credentials"));
        }
    }

    @PostMapping("auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        UserEntity user = new UserEntity();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode((dto.password())));

        Optional<Role> role = roleRepository.findByName("USER");
        if (role.isEmpty()) {
            return new ResponseEntity<>("User role not found!", HttpStatus.BAD_REQUEST);
        }

        user.setRoles(Collections.singletonList(role.get()));
        userRepository.save(user);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }
}
