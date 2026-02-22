package com.yourlight.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.yourlight.app.entity.Comentario;

import jakarta.transaction.Transactional;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    // listar comentários de um livro
    List<Comentario> findByLivroIdOrderByCriadoEmDesc(Long livroId);

    // segurança: comentário só do dono
    Optional<Comentario> findByIdAndUsuarioEmail(Long id, String email);

    @Modifying
    @Transactional
    void deleteByUsuarioEmail(String email);
}
