package com.yourlight.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.yourlight.app.dto.auth.AuthResponse;
import com.yourlight.app.dto.auth.LoginRequest;
import com.yourlight.app.dto.auth.RegisterRequest;
import com.yourlight.app.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    // REGISTRO
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        String token = service.registrar(req);
        return new AuthResponse(token);
    }

    // LOGIN
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        String token = service.login(req);
        return new AuthResponse(token);
    }
}
