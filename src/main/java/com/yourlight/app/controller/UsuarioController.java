package com.yourlight.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.yourlight.app.dto.UsuarioMeResponse;
import com.yourlight.app.dto.UsuarioMeUpdateRequest;
import com.yourlight.app.dto.UsuarioResumoResponse;
import com.yourlight.app.dto.UsuarioSenhaUpdateRequest;
import com.yourlight.app.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios/me")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // =========================
    // GET /api/usuarios/me
    // =========================
    @GetMapping
    public UsuarioMeResponse me() {
        return usuarioService.me();
    }

    // =========================
    // PUT /api/usuarios/me
    // =========================
    @PutMapping
    public UsuarioMeResponse atualizarMe(@Valid @RequestBody UsuarioMeUpdateRequest req) {
        return usuarioService.atualizarMe(req);
    }

    // =========================
    // PUT /api/usuarios/me/senha
    // =========================
    @PutMapping("/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarSenha(@Valid @RequestBody UsuarioSenhaUpdateRequest req) {
        usuarioService.atualizarSenha(req);
    }

    // =========================
    // DELETE /api/usuarios/me
    // =========================
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarConta() {
        usuarioService.deletarConta();
    }

    // =========================
    // GET /api/usuarios/me/resumo
    // =========================
    @GetMapping("/resumo")
    public UsuarioResumoResponse resumo() {
        return usuarioService.resumo();
    }
}
