package com.yourlight.app.dto;

import jakarta.validation.constraints.NotBlank;

public class LivroRequest {

    @NotBlank
    @com.fasterxml.jackson.annotation.JsonProperty("title")
    private String titulo;

    @NotBlank
    private String autor;

    private String isbn;
    @com.fasterxml.jackson.annotation.JsonProperty("description")
    private String descricao;

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
