package com.yourlight.app.dto;

public class LivroResponse {

    private Long id;
    private String titulo;
    private String autor;
    private String isbn;
    private String descricao;
    private boolean favorito;

    public LivroResponse(
        Long id,
        String titulo,
        String autor,
        String isbn,
        String descricao,
        boolean favorito
    ) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.descricao = descricao;
        this.favorito = favorito;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getIsbn() { return isbn; }
    public String getDescricao() { return descricao; }
    public boolean isFavorito() { return favorito; }
}
