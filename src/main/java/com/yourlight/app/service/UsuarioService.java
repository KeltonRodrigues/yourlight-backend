package com.yourlight.app.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yourlight.app.dto.UsuarioMeResponse;
import com.yourlight.app.dto.UsuarioMeUpdateRequest;
import com.yourlight.app.dto.UsuarioResumoResponse;
import com.yourlight.app.dto.UsuarioSenhaUpdateRequest;
import com.yourlight.app.entity.Usuario;
import com.yourlight.app.exception.BadRequestException;
import com.yourlight.app.exception.ResourceNotFoundException;
import com.yourlight.app.repository.ComentarioRepository;
import com.yourlight.app.repository.LivroRepository;
import com.yourlight.app.repository.ResenhaRepository;
import com.yourlight.app.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;
    private final LivroRepository livroRepo;
    private final ComentarioRepository comentarioRepo;
    private final ResenhaRepository resenhaRepo;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepo,
            LivroRepository livroRepo,
            ComentarioRepository comentarioRepo,
            ResenhaRepository resenhaRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepo = usuarioRepo;
        this.livroRepo = livroRepo;
        this.comentarioRepo = comentarioRepo;
        this.resenhaRepo = resenhaRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // GET /usuarios/me
    // =========================
    @Transactional(readOnly = true)
    public UsuarioMeResponse me() {
        Usuario usuario = getUsuarioLogado();
        return new UsuarioMeResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    // =========================
    // PUT /usuarios/me
    // =========================
    @Transactional
    public UsuarioMeResponse atualizarMe(UsuarioMeUpdateRequest req) {
        Usuario usuario = getUsuarioLogado();
        usuario.setNome(req.getNome().trim());

        Usuario salvo = usuarioRepo.save(usuario);
        return new UsuarioMeResponse(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    // =========================
    // PUT /usuarios/me/senha
    // =========================
    @Transactional
    public void atualizarSenha(UsuarioSenhaUpdateRequest req) {
        Usuario usuario = getUsuarioLogado();

        if (!passwordEncoder.matches(req.getSenhaAtual(), usuario.getSenhaHash())) {
            throw new BadRequestException("Senha atual incorreta.");
        }

        usuario.setSenhaHash(passwordEncoder.encode(req.getNovaSenha()));
        usuarioRepo.save(usuario);
    }

    // =========================
    // DELETE /usuarios/me
    // =========================
    @Transactional
    public void deletarConta() {
        Usuario usuario = getUsuarioLogado();
        String email = usuario.getEmail();

        // ordem correta pra evitar FK error
        resenhaRepo.deleteByUsuarioEmail(email);
        comentarioRepo.deleteByUsuarioEmail(email);
        livroRepo.deleteByUsuarioEmail(email);

        usuarioRepo.delete(usuario);
    }

    // =========================
    // GET /usuarios/me/resumo
    // =========================
    @Transactional(readOnly = true)
    public UsuarioResumoResponse resumo() {
        String email = getUsuarioLogadoEmail();

        long totalLivros = livroRepo.countByUsuarioEmail(email);
        long totalFavoritos = livroRepo.countByUsuarioEmailAndFavoritoTrue(email);

        return new UsuarioResumoResponse(totalLivros, totalFavoritos);
    }

    // =========================
    // Helpers
    // =========================
    private Usuario getUsuarioLogado() {
        String email = getUsuarioLogadoEmail();
        return usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
    }

    private String getUsuarioLogadoEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
            throw new BadRequestException("Usuário não autenticado.");
        }
        return auth.getName(); // subject do JWT (email)
    }
}
