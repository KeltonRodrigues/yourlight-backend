package com.yourlight.app.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.yourlight.app.dto.ResenhaResponse;
import com.yourlight.app.service.ResenhaService;

@RestController
@RequestMapping("/api/usuarios/me/resenhas")
public class ResenhaController {

    private final ResenhaService resenhaService;

    public ResenhaController(ResenhaService resenhaService) {
        this.resenhaService = resenhaService;
    }

    // GET /api/usuarios/me/resenhas/ultima
    @GetMapping("/ultima")
    public ResenhaResponse ultimaResenha() {
        return resenhaService.ultimaResenha();
    }

    // GET /api/usuarios/me/resenhas?page=0&size=10
    @GetMapping
    public Page<ResenhaResponse> historico(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return resenhaService.historico(page, size);
    }
}
