package com.example.echo.bring2me;

/**
 * Created by thomas on 16/09/16.
 */

public class Viagem {
    private String origem;
    private String destino;
    private String thumbnailUrl;
    private String id;
    private int avaliacaoViajante;
    private double precoBase;

    public Viagem() {
    }

    public Viagem(String origem, String destino, String thumbnailUrl, int avaliacaoViajante,
                 double precoBase, String id) {
        this.origem = origem;
        this.destino = destino;
        this.thumbnailUrl = thumbnailUrl;
        this.avaliacaoViajante = avaliacaoViajante;
        this.precoBase = precoBase;
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getAvaliacaoViajante() {
        return avaliacaoViajante;
    }

    public void setAvaliacaoViajante(int avaliacaoViajante) { this.avaliacaoViajante = avaliacaoViajante; }

    public void setYear(int avaliacaoViajante) {
        this.avaliacaoViajante = avaliacaoViajante;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

}
