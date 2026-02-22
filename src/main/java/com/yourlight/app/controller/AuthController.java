package com.yourlight.app.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.yourlight.app.dto.auth.LoginRequest;
import com.yourlight.app.dto.auth.RegisterRequest;
import com.yourlight.app.dto.auth.AuthResponse;
import com.yourlight.app.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        AuthService.AuthTokens tokens = service.registrar(req);
        return new AuthResponse(tokens.accessToken(), tokens.refreshToken());
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        AuthService.AuthTokens tokens = service.login(req);
        return new AuthResponse(tokens.accessToken(), tokens.refreshToken());
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody String refreshToken) {
        AuthService.AuthTokens tokens = service.refresh(refreshToken);
        return new AuthResponse(tokens.accessToken(), tokens.refreshToken());
    }
}