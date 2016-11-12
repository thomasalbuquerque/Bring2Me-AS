package com.example.echo.bring2me.model;

/**
 * Created by thomas on 06/11/16.
 */

public class Pedido {
    private String nomePedido;
    private String valorPedido;
    private String linkPedido;
    private String idViagem;
    private int idPedido;
    private int empacotadoPedido;
    private int Avaliado;
    private int Aceito;
    private int correioOuPessoalPedido;
    private String emailUsuarioPedidoi;
    private String enderecoPedido;

    public Pedido() {
    }

    public Pedido(String nomePedido, String valorPedido, String linkPedido, String idViagem, int correioOuPessoalPedido, String emailUsuarioPedidoi, String enderecoPedido, int naoPacotoPedido, int idPedido) {
        this.nomePedido = nomePedido;
        this.valorPedido = valorPedido;
        this.linkPedido = linkPedido;
        this.idViagem = idViagem;
        this.correioOuPessoalPedido = correioOuPessoalPedido;
        this.emailUsuarioPedidoi = emailUsuarioPedidoi;
        this.enderecoPedido = enderecoPedido;
        this.empacotadoPedido = naoPacotoPedido;
        this.idPedido = idPedido;
    }
    public String getEnderecoPedido() {
        return enderecoPedido;
    }

    public void setEnderecoPedido(String enderecoPedido) {
        this.enderecoPedido = enderecoPedido;
    }

    public String getEmailUsuarioPedido() {
        return emailUsuarioPedidoi;
    }

    public void setEmailUsuarioPedidoi(String emailUsuarioPedidoi) {
        this.emailUsuarioPedidoi = emailUsuarioPedidoi;
    }

    public int getCorreioOuPessoalPedido() {
        return correioOuPessoalPedido;
    }

    public void setCorreioOuPessoalPedido(int correioOuPessoalPedido) {
        this.correioOuPessoalPedido = correioOuPessoalPedido;
    }

    public int getEmpacotadoPedido() {
        return empacotadoPedido;
    }

    public void setEmpacotadoPedido(int empacotadoPedido) {
        this.empacotadoPedido = empacotadoPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdViagem() {
        return idViagem;
    }

    public void setIdViagem(String idViagem) {
        this.idViagem = idViagem;
    }

    public String getLinkPedido() {
        return linkPedido;
    }

    public void setLinkPedido(String linkPedido) {
        this.linkPedido = linkPedido;
    }

    public String getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(String valorPedido) {
        this.valorPedido = valorPedido;
    }

    public String getNomePedido() {
        return nomePedido;
    }

    public void setNomePedido(String nomePedido) {
        this.nomePedido = nomePedido;
    }

    public int getAvaliado() {return Avaliado;}

    public void setAvaliado(int avaliado) {Avaliado = avaliado;}

    public int getAceito() {return Aceito;}

    public void setAceito(int aceito) {Aceito = aceito;}


}
