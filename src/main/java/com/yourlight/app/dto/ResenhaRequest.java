package com.yourlight.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ResenhaRequest {

    @NotBlank(message = "O texto é obrigatório")
    @Size(max = 4000, message = "O texto deve ter no máximo 4000 caracteres")
    private String texto;

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "A nota deve ser no mínimo 1")
    @Max(value = 5, message = "A nota deve ser no máximo 5")
    private Integer nota;

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
}
