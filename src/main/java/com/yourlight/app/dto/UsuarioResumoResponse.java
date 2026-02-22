package com.yourlight.app.dto;

public class UsuarioResumoResponse {

    private long totalLivros;
    private long totalFavoritos;

    public UsuarioResumoResponse(long totalLivros, long totalFavoritos) {
        this.totalLivros = totalLivros;
        this.totalFavoritos = totalFavoritos;
    }

    public long getTotalLivros() { return totalLivros; }
    public long getTotalFavoritos() { return totalFavoritos; }
}
