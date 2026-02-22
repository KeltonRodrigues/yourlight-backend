package com.yourlight.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourlight.app.dto.ResenhaRequest;
import com.yourlight.app.dto.ResenhaResponse;
import com.yourlight.app.entity.Livro;
import com.yourlight.app.entity.Resenha;
import com.yourlight.app.entity.Usuario;
import com.yourlight.app.exception.BadRequestException;
import com.yourlight.app.exception.ResourceNotFoundException;
import com.yourlight.app.repository.LivroRepository;
import com.yourlight.app.repository.ResenhaRepository;
import com.yourlight.app.repository.UsuarioRepository;

@Service
public class ResenhaService {

    private final ResenhaRepository resenhaRepo;
    private final LivroRepository livroRepo;
    private final UsuarioRepository usuarioRepo;

    public ResenhaService(ResenhaRepository resenhaRepo,
                          LivroRepository livroRepo,
                          UsuarioRepository usuarioRepo) {
        this.resenhaRepo = resenhaRepo;
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // =========================
    // GET /api/livros/{livroId}/resenha (minha resenha desse livro)
    // =========================
    @Transactional(readOnly = true)
    public ResenhaResponse buscarPorLivro(Long livroId) {
        String email = getUsuarioLogadoEmail();

        livroRepo.findByIdAndUsuarioEmail(livroId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + livroId));

        Resenha r = resenhaRepo.findByLivroIdAndUsuarioEmail(livroId, email)
                .orElse(null);

        return (r == null) ? null : toResponse(r);
    }

    // =========================
    // PUT /api/livros/{livroId}/resenha  (cria OU atualiza)
    // =========================
    @Transactional
    public ResenhaResponse criarOuAtualizar(Long livroId, ResenhaRequest req) {
        String email = getUsuarioLogadoEmail();

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Livro livro = livroRepo.findByIdAndUsuarioEmail(livroId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + livroId));

        Resenha r = resenhaRepo.findByLivroIdAndUsuarioEmail(livroId, email)
                .orElseGet(Resenha::new);

        if (r.getId() == null) {
            r.setLivro(livro);
            r.setUsuario(usuario);
        }

        r.setTexto(req.getTexto());
        r.setNota(req.getNota());

        return toResponse(resenhaRepo.save(r));
    }

    // =========================
    // DELETE /api/livros/{livroId}/resenha
    // =========================
    @Transactional
    public void deletar(Long livroId) {
        String email = getUsuarioLogadoEmail();

        // garante que o livro é do usuário logado
        livroRepo.findByIdAndUsuarioEmail(livroId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + livroId));

        boolean existe = resenhaRepo.existsByLivroIdAndUsuarioEmail(livroId, email);
        if (!existe) {
            throw new ResourceNotFoundException("Resenha não encontrada para este livro.");
        }

        // mais eficiente
        resenhaRepo.deleteByLivroIdAndUsuarioEmail(livroId, email);
    }

    // =========================
    // GET /api/usuarios/me/resenhas/ultima
    // =========================
    @Transactional(readOnly = true)
    public ResenhaResponse ultimaResenha() {
        String email = getUsuarioLogadoEmail();

        Resenha r = resenhaRepo.findTopByUsuarioEmailOrderByAtualizadoEmDesc(email)
                .orElse(null);

        return (r == null) ? null : toResponse(r);
    }

    // =========================
    // GET /api/usuarios/me/resenhas?page=0&size=10
    // =========================
    @Transactional(readOnly = true)
    public Page<ResenhaResponse> historico(int page, int size) {
        String email = getUsuarioLogadoEmail();

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "atualizadoEm")
        );

        return resenhaRepo.findByUsuarioEmailOrderByAtualizadoEmDesc(email, pageable)
                .map(this::toResponse);
    }

    private ResenhaResponse toResponse(Resenha r) {
        return new ResenhaResponse(
                r.getId(),
                r.getLivro().getId(),
                r.getLivro().getTitulo(),
                r.getTexto(),
                r.getNota(),
                r.getCriadoEm(),
                r.getAtualizadoEm()
        );
    }

    private String getUsuarioLogadoEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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
