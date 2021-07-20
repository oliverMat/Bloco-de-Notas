package br.oliver.notepad.model;

import java.io.Serializable;

public class Tarefa implements Serializable {

    private Long id;
    private String nomeTabela;
    private String nomeTarefa;
    private String nomeDescricao;
    private String cor;
    private String dataCriacao;
    private String horaCriacao;
    private String comSenha;
    private String senhaNota;
    private String comAudio;
    private String caminhoAudio;
    private String comImagem;
    private String caminhoImagem;
    private String favorito;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    public String getNomeTarefa() {
        return nomeTarefa;
    }

    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }

    public String getNomeDescricao() {
        return nomeDescricao;
    }

    public void setNomeDescricao(String nomeDescricao) {
        this.nomeDescricao = nomeDescricao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getHoraCriacao() {
        return horaCriacao;
    }

    public void setHoraCriacao(String horaCriacao) {
        this.horaCriacao = horaCriacao;
    }

    public String getComSenha() {
        return comSenha;
    }

    public void setComSenha(String comSenha) {
        this.comSenha = comSenha;
    }

    public String getSenhaNota() {
        return senhaNota;
    }

    public void setSenhaNota(String senhaNota) {
        this.senhaNota = senhaNota;
    }

    public String getComAudio() {
        return comAudio;
    }

    public void setComAudio(String comAudio) {
        this.comAudio = comAudio;
    }

    public String getCaminhoAudio() {
        return caminhoAudio;
    }

    public void setCaminhoAudio(String caminhoAudio) {
        this.caminhoAudio = caminhoAudio;
    }

    public String getComImagem() {
        return comImagem;
    }

    public void setComImagem(String comImagem) {
        this.comImagem = comImagem;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getFavorito() {
        return favorito;
    }

    public void setFavorito(String favorito) {
        this.favorito = favorito;
    }
}
