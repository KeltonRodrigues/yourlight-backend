package com.yourlight.app.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final String issuer;
    private final long accessExpirationMinutes;
    private final long refreshExpirationDays;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.expiration-minutes}") long accessExpirationMinutes,
            @Value("${app.jwt.refresh-expiration-days}") long refreshExpirationDays
    ) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.issuer = issuer;
        this.accessExpirationMinutes = accessExpirationMinutes;
        this.refreshExpirationDays = refreshExpirationDays;

        this.verifier = JWT.require(this.algorithm)
                .withIssuer(this.issuer)
                .build();
    }

    public String gerarAccessToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessExpirationMinutes, ChronoUnit.MINUTES);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .withClaim("type", "access")
                .sign(algorithm);
    }

    public String gerarRefreshToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(refreshExpirationDays, ChronoUnit.DAYS);

        return JWT.create()
                .withIssuer(issuer)
                .withSubject(email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(exp))
                .withClaim("type", "refresh")
                .sign(algorithm);
    }

    public String validarEExtrairEmail(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido ou expirado");
        }
    }

    public boolean isRefreshToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        return "refresh".equals(jwt.getClaim("type").asString());
    }
}