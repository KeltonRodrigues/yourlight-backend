package com.yourlight.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.yourlight.app.dto.ComentarioRequest;
import com.yourlight.app.dto.ComentarioResponse;
import com.yourlight.app.service.ComentarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Validated
public class ComentarioController {

    private final ComentarioService service;

    public ComentarioController(ComentarioService service) {
        this.service = service;
    }

    // =========================
    // GET /api/livros/{livroId}/comentarios
    // =========================
    @GetMapping("/livros/{livroId}/comentarios")
    public List<ComentarioResponse> listarPorLivro(@PathVariable Long livroId) {
        return service.listarPorLivro(livroId);
    }

    // =========================
    // POST /api/livros/{livroId}/comentarios
    // =========================
    @PostMapping("/livros/{livroId}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public ComentarioResponse criar(@PathVariable Long livroId, @Valid @RequestBody ComentarioRequest req) {
        return service.criar(livroId, req);
    }

    // =========================
    // PUT /api/comentarios/{id}
    // =========================
    @PutMapping("/comentarios/{id}")
    public ComentarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody ComentarioRequest req) {
        return service.atualizar(id, req);
    }

    // =========================
    // DELETE /api/comentarios/{id}
    // =========================
    @DeleteMapping("/comentarios/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
