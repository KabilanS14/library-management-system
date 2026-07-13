package com.librarymanagement.backend.service;

import com.librarymanagement.backend.dto.AuthenticationResponse;
import com.librarymanagement.backend.dto.LoginRequest;
import com.librarymanagement.backend.dto.RegisterRequest;
import com.librarymanagement.backend.entity.User;
import com.librarymanagement.backend.enums.Role;
import com.librarymanagement.backend.exception.DuplicateResourceException;
import com.librarymanagement.backend.repository.UserRepository;
import com.librarymanagement.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerShouldCreateUserAndReturnToken() {
        RegisterRequest request = RegisterRequest.builder()
                .name("John")
                .email("john@example.com")
                .password("password123")
                .build();

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(jwtService.generateToken("john@example.com")).thenReturn("token");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthenticationResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("token", response.getToken());
        assertEquals(Role.MEMBER, response.getUser().getRole());
    }

    @Test
    void registerShouldThrowWhenUserExists() {
        RegisterRequest request = RegisterRequest.builder().email("john@example.com").build();
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.register(request));
    }

    @Test
    void loginShouldReturnTokenForValidUser() {
        LoginRequest request = LoginRequest.builder().email("john@example.com").password("password123").build();
        User user = User.builder().id(1L).email("john@example.com").password("encoded").role(Role.MEMBER).build();

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("john@example.com")).thenReturn("token");

        AuthenticationResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("token", response.getToken());
    }
}
