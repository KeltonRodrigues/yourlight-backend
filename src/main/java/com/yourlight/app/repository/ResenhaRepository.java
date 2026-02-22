package com.yourlight.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yourlight.app.entity.Resenha;

public interface ResenhaRepository extends JpaRepository<Resenha, Long> {

    // ✅ endpoint "última resenha"
    Optional<Resenha> findTopByUsuarioEmailOrderByAtualizadoEmDesc(String email);

    // ✅ histórico paginado
    Page<Resenha> findByUsuarioEmailOrderByAtualizadoEmDesc(String email, Pageable pageable);

    // ✅ CRUD por livro (1 resenha por livro por usuário)
    Optional<Resenha> findByLivroIdAndUsuarioEmail(Long livroId, String email);

    boolean existsByLivroIdAndUsuarioEmail(Long livroId, String email);

    // ✅ delete conta (remove tudo do usuário)
    void deleteByUsuarioEmail(String email);

    // ✅ DELETE da resenha daquele livro (sem carregar entidade)
    void deleteByLivroIdAndUsuarioEmail(Long livroId, String email);

    // (opcional) se você quiser trabalhar por id de resenha
    Optional<Resenha> findByIdAndUsuarioEmail(Long id, String email);
}
