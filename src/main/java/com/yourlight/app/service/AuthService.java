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

    // REGISTRO + LOGIN AUTOMÁTICO
    @Transactional
    public String registrar(RegisterRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        if (usuarioRepo.existsByEmail(email)) {
            throw new BadRequestException("Já existe usuário com esse email.");
        }

        Usuario u = new Usuario();
        u.setNome(req.getNome().trim());
        u.setEmail(email);
        u.setSenhaHash(passwordEncoder.encode(req.getSenha()));

        usuarioRepo.save(u);

        // retorna token já autenticado
        return jwtService.gerarToken(u.getEmail());
    }

    // LOGIN
    @Transactional(readOnly = true)
    public String login(LoginRequest req) {

        String email = req.getEmail().trim().toLowerCase();

        Usuario u = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (!passwordEncoder.matches(req.getSenha(), u.getSenhaHash())) {
            throw new BadRequestException("Email ou senha inválidos.");
        }

        return jwtService.gerarToken(u.getEmail());
    }
}
