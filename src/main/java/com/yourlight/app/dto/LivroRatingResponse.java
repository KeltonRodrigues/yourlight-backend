package com.yourlight.app.dto;

public class LivroRatingResponse {

    private Long livroId;
    private double media;      // 0.0 quando não tem resenhas
    private long totalResenhas;

    public LivroRatingResponse(Long livroId, double media, long totalResenhas) {
        this.livroId = livroId;
        this.media = media;
        this.totalResenhas = totalResenhas;
    }

    public Long getLivroId() { return livroId; }
    public double getMedia() { return media; }
    public long getTotalResenhas() { return totalResenhas; }
}
