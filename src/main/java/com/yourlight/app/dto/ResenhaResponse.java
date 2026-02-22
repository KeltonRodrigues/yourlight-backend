package com.yourlight.app.dto;

import java.time.LocalDateTime;

public class ResenhaResponse {
    private Long id;
    private Long livroId;
    private String livroTitulo;
    private String texto;
    private Integer nota;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public ResenhaResponse(Long id, Long livroId, String livroTitulo, String texto, Integer nota,
                           LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.livroId = livroId;
        this.livroTitulo = livroTitulo;
        this.texto = texto;
        this.nota = nota;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() { return id; }
    public Long getLivroId() { return livroId; }
    public String getLivroTitulo() { return livroTitulo; }
    public String getTexto() { return texto; }
    public Integer getNota() { return nota; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
