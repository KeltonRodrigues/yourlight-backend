package com.yourlight.app.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @Email
    @NotBlank
    @com.fasterxml.jackson.annotation.JsonProperty("email")
    private String email;

    @NotBlank
    @com.fasterxml.jackson.annotation.JsonProperty("password")
    private String senha;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}
