package com.yourlight.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yourlight.app.entity.Livro;

import jakarta.transaction.Transactional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Optional<Livro> findByIdAndUsuarioEmail(Long id, String email);
    @Modifying
    @Transactional
    void deleteByUsuarioEmail(String email);

    long countByUsuarioEmail(String email);
    long countByUsuarioEmailAndFavoritoTrue(String email);

    Page<Livro> findByUsuarioEmailAndFavoritoTrue(String email, Pageable pageable);

    @Query("""
        select l
        from Livro l
        where l.usuario.email = :email
          and (
                :q = '' or
                lower(l.titulo) like lower(concat('%', :q, '%')) or
                lower(l.autor)  like lower(concat('%', :q, '%')) or
                lower(coalesce(l.isbn, '')) like lower(concat('%', :q, '%'))
          )
    """)
    Page<Livro> searchByUsuarioEmail(@Param("email") String email,
                                    @Param("q") String q,
                                    Pageable pageable);
}
