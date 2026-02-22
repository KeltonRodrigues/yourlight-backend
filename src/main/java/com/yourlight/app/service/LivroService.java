package com.yourlight.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourlight.app.dto.LivroRequest;
import com.yourlight.app.dto.LivroResponse;
import com.yourlight.app.entity.Livro;
import com.yourlight.app.entity.Usuario;
import com.yourlight.app.exception.BadRequestException;
import com.yourlight.app.exception.ResourceNotFoundException;
import com.yourlight.app.repository.LivroRepository;
import com.yourlight.app.repository.UsuarioRepository;

@Service
public class LivroService {

    private final LivroRepository repo;
    private final UsuarioRepository usuarioRepo;

    public LivroService(LivroRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
    }

    // =========================
    // GET /livros
    // =========================
    @Transactional(readOnly = true)
    public List<LivroResponse> listar() {
        return listarPaginado(0, 20, "titulo", "asc", "").getContent();
    }

    // =========================
    // GET /livros/{id}
    // =========================
    @Transactional(readOnly = true)
    public LivroResponse buscarPorId(Long id) {
        String email = getUsuarioLogadoEmail();

        Livro livro = repo.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));

        return toResponse(livro);
    }

    // =========================
    // POST /livros
    // =========================
    @Transactional
    public LivroResponse criar(LivroRequest req) {
        String email = getUsuarioLogadoEmail();

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Livro livro = new Livro();
        livro.setTitulo(req.getTitulo());
        livro.setAutor(req.getAutor());
        livro.setIsbn(req.getIsbn());
        livro.setDescricao(req.getDescricao());
        livro.setUsuario(usuario);
        // favorito default false no entity

        return toResponse(repo.save(livro));
    }

    // =========================
    // PUT /livros/{id}
    // =========================
    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest req) {
        String email = getUsuarioLogadoEmail();

        Livro livro = repo.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));

        livro.setTitulo(req.getTitulo());
        livro.setAutor(req.getAutor());
        livro.setIsbn(req.getIsbn());
        livro.setDescricao(req.getDescricao());

        return toResponse(repo.save(livro));
    }

    // =========================
    // DELETE /livros/{id}
    // =========================
    @Transactional
    public void deletar(Long id) {
        String email = getUsuarioLogadoEmail();

        Livro livro = repo.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));

        repo.delete(livro);
    }

    // =========================
    // ❤️ FAVORITAR
    // =========================
    @Transactional
    public void favoritar(Long id) {
        String email = getUsuarioLogadoEmail();

        Livro livro = repo.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));

        if (!livro.isFavorito()) {
            livro.setFavorito(true);
            repo.save(livro);
        }
    }

    // =========================
    // 💔 DESFAVORITAR
    // =========================
    @Transactional
    public void desfavoritar(Long id) {
        String email = getUsuarioLogadoEmail();

        Livro livro = repo.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + id));

        if (livro.isFavorito()) {
            livro.setFavorito(false);
            repo.save(livro);
        }
    }

    // =========================
    // ⭐ LISTAR FAVORITOS (PAGINADO)
    // =========================
    @Transactional(readOnly = true)
    public Page<LivroResponse> listarFavoritos(int page, int size, String sort, String dir) {
        String email = getUsuarioLogadoEmail();

        Sort.Direction direction = "desc".equalsIgnoreCase(dir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Livro> result = repo.findByUsuarioEmailAndFavoritoTrue(email, pageable);
        return result.map(this::toResponse);
    }

    // =========================
    // LISTAGEM PAGINADA (search)
    // =========================
    @Transactional(readOnly = true)
    public Page<LivroResponse> listarPaginado(int page, int size, String sort, String dir, String q) {
        String email = getUsuarioLogadoEmail();

        Sort.Direction direction = "desc".equalsIgnoreCase(dir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        String query = (q == null) ? "" : q;

        Page<Livro> result = repo.searchByUsuarioEmail(email, query, pageable);
        return result.map(this::toResponse);
    }

    // =========================
    // ENTITY -> DTO
    // =========================
    private LivroResponse toResponse(Livro livro) {
        return new LivroResponse(
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getIsbn(),
                livro.getDescricao(),
                livro.isFavorito()
        );
    }

    // =========================
    // USUÁRIO LOGADO (JWT subject=email)
    // =========================
    private String getUsuarioLogadoEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // bloqueia anonymousUser (causa GET vazio sem token)
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new BadRequestException("Usuário não autenticado.");
        }

        String name = auth.getName();
        if (name == null || name.isBlank() || "anonymousUser".equalsIgnoreCase(name)) {
            throw new BadRequestException("Usuário não autenticado.");
        }

        return name.trim().toLowerCase();
    }
}
