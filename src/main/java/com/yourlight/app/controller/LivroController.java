package com.yourlight.app.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.yourlight.app.dto.LivroRequest;
import com.yourlight.app.dto.LivroResponse;
import com.yourlight.app.service.LivroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    // =========================
    // POST /livros/{id}/favorito  (marca como favorito)
    // =========================
    @PostMapping("/{id}/favorito")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void favoritar(@PathVariable Long id) {
        livroService.favoritar(id);
    }

    // =========================
    // DELETE /livros/{id}/favorito (remove favorito)
    // =========================
    @DeleteMapping("/{id}/favorito")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desfavoritar(@PathVariable Long id) {
        livroService.desfavoritar(id);
    }

    // =========================
    // POST /livros
    // =========================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponse criar(@Valid @RequestBody LivroRequest req) {
        return livroService.criar(req);
    }

    // =========================
    // GET /livros
    // =========================
    @GetMapping
    public List<LivroResponse> listar() {
        return livroService.listar();
    }

    // =========================
    // GET /livros/paginado?page=0&size=20&sort=titulo&dir=asc&q=...
    // =========================
    @GetMapping("/paginado")
    public Page<LivroResponse> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "titulo") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(defaultValue = "") String q
    ) {
        return livroService.listarPaginado(page, size, sort, dir, q);
    }

    // =========================
    // GET /livros/favoritos?page=0&size=20&sort=titulo&dir=asc
    // =========================
    @GetMapping("/favoritos")
    public Page<LivroResponse> listarFavoritos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "titulo") String sort,
            @RequestParam(defaultValue = "asc") String dir
    ) {
        return livroService.listarFavoritos(page, size, sort, dir);
    }

    // =========================
    // GET /livros/{id}
    // =========================
    @GetMapping("/{id}")
    public LivroResponse buscar(@PathVariable Long id) {
        return livroService.buscarPorId(id);
    }

    // =========================
    // PUT /livros/{id}
    // =========================
    @PutMapping("/{id}")
    public LivroResponse atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequest req) {
        return livroService.atualizar(id, req);
    }

    // =========================
    // DELETE /livros/{id}
    // =========================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        livroService.deletar(id);
    }
}
