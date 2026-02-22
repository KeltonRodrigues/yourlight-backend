package com.yourlight.app.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    @com.fasterxml.jackson.annotation.JsonProperty("name")
    private String nome;

    @Email
    @NotBlank
    @com.fasterxml.jackson.annotation.JsonProperty("email")
    private String email;

    @NotBlank
    @Size(min = 6)
    @com.fasterxml.jackson.annotation.JsonProperty("password")
    private String senha;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
