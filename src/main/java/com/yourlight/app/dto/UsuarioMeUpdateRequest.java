package com.yourlight.app.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioMeUpdateRequest {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}
