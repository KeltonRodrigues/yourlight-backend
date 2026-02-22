package com.yourlight.app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourlight.app.dto.auth.LoginRequest;
import com.yourlight.app.dto.auth.RegisterRequest;
import com.yourlight.app.entity.Usuario;
import com.yourlight.app.exception.BadRequestException;
import com.yourlight.app.exception.ResourceNotFoundException;
import com.yourlight.app.repository.UsuarioRepository;
import com.yourlight.app.security.JwtService;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepo,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthTokens registrar(RegisterRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        if (usuarioRepo.existsByEmail(email)) {
            throw new BadRequestException("Já existe usuário com esse email.");
        }

        Usuario u = new Usuario();
        u.setNome(req.getNome().trim());
        u.setEmail(email);
        u.setSenhaHash(passwordEncoder.encode(req.getSenha()));

        usuarioRepo.save(u);

        return gerarTokens(u.getEmail());
    }

    @Transactional(readOnly = true)
    public AuthTokens login(LoginRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        Usuario u = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(req.getSenha(), u.getSenhaHash())) {
            throw new BadRequestException("Email ou senha inválidos.");
        }

        return gerarTokens(u.getEmail());
    }

    public AuthTokens refresh(String refreshToken) {

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new BadRequestException("Token inválido.");
        }

        String email = jwtService.validarEExtrairEmail(refreshToken);

        return gerarTokens(email);
    }

    private AuthTokens gerarTokens(String email) {
        String accessToken = jwtService.gerarAccessToken(email);
        String refreshToken = jwtService.gerarRefreshToken(email);
        return new AuthTokens(accessToken, refreshToken);
    }

    public record AuthTokens(String accessToken, String refreshToken) {}
}