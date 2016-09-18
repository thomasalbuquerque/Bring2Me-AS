package com.example.echo.bring2me.listview.model;

/**
 * Created by thomas on 16/09/16.
 */

import java.util.ArrayList;

public class Viagem {
    private String origem, destino, thumbnailUrl;
    private int avaliacaoViajante;
    private double precoBase;

    public Viagem() {
    }

    public Viagem(String origem, String destino, String thumbnailUrl, int avaliacaoViajante,
                 double precoBase) {
        this.origem = origem;
        this.destino = destino;
        this.thumbnailUrl = thumbnailUrl;
        this.avaliacaoViajante = avaliacaoViajante;
        this.precoBase = precoBase;
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

}
