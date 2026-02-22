package com.yourlight.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.yourlight.app.dto.ResenhaRequest;
import com.yourlight.app.dto.ResenhaResponse;
import com.yourlight.app.service.ResenhaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/livros")
public class LivroResenhaController {

    private final ResenhaService resenhaService;

    public LivroResenhaController(ResenhaService resenhaService) {
        this.resenhaService = resenhaService;
    }

    // GET /api/livros/{livroId}/resenha
    @GetMapping("/{livroId}/resenha")
    public ResenhaResponse buscarPorLivro(@PathVariable Long livroId) {
        return resenhaService.buscarPorLivro(livroId);
    }

    // PUT /api/livros/{livroId}/resenha  (cria OU atualiza)
    @PutMapping("/{livroId}/resenha")
    public ResenhaResponse criarOuAtualizar(
            @PathVariable Long livroId,
            @Valid @RequestBody ResenhaRequest req
    ) {
        return resenhaService.criarOuAtualizar(livroId, req);
    }

    // DELETE /api/livros/{livroId}/resenha
    @DeleteMapping("/{livroId}/resenha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long livroId) {
        resenhaService.deletar(livroId);
    }
}
