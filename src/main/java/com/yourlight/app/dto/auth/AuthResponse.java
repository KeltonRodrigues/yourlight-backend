package com.yourlight.app.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {}