package com.yourlight.app.dto;

import jakarta.validation.constraints.NotBlank;

public class ComentarioRequest {

    @NotBlank
    private String texto;

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
}
