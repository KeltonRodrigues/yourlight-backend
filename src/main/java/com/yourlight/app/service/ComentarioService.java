package com.yourlight.app.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourlight.app.dto.ComentarioRequest;
import com.yourlight.app.dto.ComentarioResponse;
import com.yourlight.app.entity.Comentario;
import com.yourlight.app.entity.Livro;
import com.yourlight.app.entity.Usuario;
import com.yourlight.app.exception.BadRequestException;
import com.yourlight.app.exception.ResourceNotFoundException;
import com.yourlight.app.repository.ComentarioRepository;
import com.yourlight.app.repository.LivroRepository;
import com.yourlight.app.repository.UsuarioRepository;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepo;
    private final LivroRepository livroRepo;
    private final UsuarioRepository usuarioRepo;

    public ComentarioService(ComentarioRepository comentarioRepo,
                             LivroRepository livroRepo,
                             UsuarioRepository usuarioRepo) {
        this.comentarioRepo = comentarioRepo;
        this.livroRepo = livroRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional(readOnly = true)
    public List<ComentarioResponse> listarPorLivro(Long livroId) {
        String email = getUsuarioLogadoEmail();

        livroRepo.findByIdAndUsuarioEmail(livroId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + livroId));

        return comentarioRepo.findByLivroIdOrderByCriadoEmDesc(livroId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ComentarioResponse criar(Long livroId, ComentarioRequest req) {
        String email = getUsuarioLogadoEmail();

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        Livro livro = livroRepo.findByIdAndUsuarioEmail(livroId, email)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado: " + livroId));

        Comentario c = new Comentario();
        c.setTexto(req.getTexto());
        c.setLivro(livro);
        c.setUsuario(usuario);

        return toResponse(comentarioRepo.save(c));
    }

    @Transactional
    public ComentarioResponse atualizar(Long id, ComentarioRequest req) {
        String email = getUsuarioLogadoEmail();

        Comentario c = comentarioRepo.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado: " + id));

        c.setTexto(req.getTexto());
        return toResponse(comentarioRepo.save(c));
    }

    @Transactional
    public void deletar(Long id) {
        String email = getUsuarioLogadoEmail();

        Comentario c = comentarioRepo.findByIdAndUsuarioEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Comentário não encontrado: " + id));

        comentarioRepo.delete(c);
    }

    private ComentarioResponse toResponse(Comentario c) {
        return new ComentarioResponse(
                c.getId(),
                c.getTexto(),
                c.getLivro().getId(),
                c.getUsuario().getId(),
                c.getUsuario().getNome(),
                c.getCriadoEm(),
                c.getAtualizadoEm()
        );
    }

    private String getUsuarioLogadoEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new BadRequestException("Usuário não autenticado.");
        }
        return auth.getName();
    }
}
