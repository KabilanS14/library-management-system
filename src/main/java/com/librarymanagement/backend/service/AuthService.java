package com.librarymanagement.backend.service;

import com.librarymanagement.backend.dto.AuthenticationResponse;
import com.librarymanagement.backend.dto.LoginRequest;
import com.librarymanagement.backend.dto.RegisterRequest;
import com.librarymanagement.backend.dto.UserResponse;
import com.librarymanagement.backend.entity.User;
import com.librarymanagement.backend.enums.Role;
import com.librarymanagement.backend.exception.DuplicateResourceException;
import com.librarymanagement.backend.exception.InvalidCredentialsException;
import com.librarymanagement.backend.repository.UserRepository;
import com.librarymanagement.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.MEMBER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(token)
                .user(UserResponse.from(user))
                .build();
    }

    @Transactional(readOnly = true)
    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail());
        return AuthenticationResponse.builder()
                .token(token)
                .user(UserResponse.from(user))
                .build();
    }
}
