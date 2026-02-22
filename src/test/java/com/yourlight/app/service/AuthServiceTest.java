package com.yourlight.app.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.yourlight.app.dto.auth.LoginRequest;
import com.yourlight.app.entity.Usuario;
import com.yourlight.app.exception.BadRequestException;
import com.yourlight.app.repository.UsuarioRepository;
import com.yourlight.app.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UsuarioRepository usuarioRepo;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    JwtService jwtService;

    @InjectMocks
    AuthService authService;

    @Test
    void deveLancarErroQuandoSenhaForInvalida() {


        LoginRequest request = new LoginRequest();
        request.setEmail("teste@email.com");
        request.setSenha("senhaErrada");

        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenhaHash("hashCorreto");

        when(usuarioRepo.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches("senhaErrada", "hashCorreto"))
                .thenReturn(false);


        assertThrows(BadRequestException.class, () -> {
            authService.login(request);
        });
    }
}