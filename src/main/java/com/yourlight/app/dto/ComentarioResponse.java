package com.yourlight.app.dto;
import java.time.LocalDateTime;

public class ComentarioResponse {
    private Long id;
    private String texto;
    private Long livroId;
    private Long usuarioId;
    private String usuarioNome;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public ComentarioResponse(
        Long id,
        String texto,
        Long livroId,
        Long usuarioId,
        String usuarioNome,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
    ) {
        this.id = id;
        this.texto = texto;
        this.livroId = livroId;
        this.usuarioId = usuarioId;
        this.usuarioNome = usuarioNome;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() { return id; }
    public String getTexto() { return texto; }
    public Long getLivroId() { return livroId; }
    public Long getUsuarioId() { return usuarioId; }
    public String getUsuarioNome() { return usuarioNome; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
}
