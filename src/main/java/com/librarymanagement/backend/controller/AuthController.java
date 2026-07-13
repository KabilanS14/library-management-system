package com.librarymanagement.backend.controller;

import com.librarymanagement.backend.dto.AuthenticationResponse;
import com.librarymanagement.backend.dto.LoginRequest;
import com.librarymanagement.backend.dto.RegisterRequest;
import com.librarymanagement.backend.service.AuthService;
import com.librarymanagement.backend.util.ApiResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "User registration and login APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new member")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<ApiResponseWrapper<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthenticationResponse response = authService.register(request);

        log.info("User registered successfully with email: {}", request.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseWrapper.<AuthenticationResponse>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<ApiResponseWrapper<AuthenticationResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthenticationResponse response = authService.login(request);

        log.info("User logged in successfully with email: {}", request.getEmail());

        return ResponseEntity.ok(
                ApiResponseWrapper.<AuthenticationResponse>builder()
                        .success(true)
                        .message("Login successful")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}