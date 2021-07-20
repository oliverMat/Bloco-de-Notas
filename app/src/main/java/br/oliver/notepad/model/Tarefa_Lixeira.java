package br.oliver.notepad.model;

import java.io.Serializable;

public class Tarefa_Lixeira implements Serializable {

    private Long id_lixeira;
    private String nomeTabela_lixeira;
    private String nomeTarefa_lixeira;
    private String nomeDescricao_lixeira;
    private String cor_lixeira;
    private String dataCriacao_lixeira;
    private String horaCriacao_lixeira;
    private String comSenha_lixeira;
    private String senhaNota_lixeira;
    private String comAudio_lixeira;
    private String caminhoAudio_lixeira;
    private String comImagem_lixeira;
    private String caminhoImagem_lixeira;

    private boolean selected;

    public Long getId_lixeira() {
        return id_lixeira;
    }

    public void setId_lixeira(Long id_lixeira) {
        this.id_lixeira = id_lixeira;
    }

    public String getNomeTabela_lixeira() {
        return nomeTabela_lixeira;
    }

    public void setNomeTabela_lixeira(String nomeTabela_lixeira) {
        this.nomeTabela_lixeira = nomeTabela_lixeira;
    }

    public String getNomeTarefa_lixeira() {
        return nomeTarefa_lixeira;
    }

    public void setNomeTarefa_lixeira(String nomeTarefa_lixeira) {
        this.nomeTarefa_lixeira = nomeTarefa_lixeira;
    }

    public String getNomeDescricao_lixeira() {
        return nomeDescricao_lixeira;
    }

    public void setNomeDescricao_lixeira(String nomeDescricao_lixeira) {
        this.nomeDescricao_lixeira = nomeDescricao_lixeira;
    }

    public String getCor_lixeira() {
        return cor_lixeira;
    }

    public void setCor_lixeira(String cor_lixeira) {
        this.cor_lixeira = cor_lixeira;
    }

    public String getDataCriacao_lixeira() {
        return dataCriacao_lixeira;
    }

    public void setDataCriacao_lixeira(String dataCriacao_lixeira) {
        this.dataCriacao_lixeira = dataCriacao_lixeira;
    }

    public String getHoraCriacao_lixeira() {
        return horaCriacao_lixeira;
    }

    public void setHoraCriacao_lixeira(String horaCriacao_lixeira) {
        this.horaCriacao_lixeira = horaCriacao_lixeira;
    }

    public String getComSenha_lixeira() {
        return comSenha_lixeira;
    }

    public void setComSenha_lixeira(String comSenha_lixeira) {
        this.comSenha_lixeira = comSenha_lixeira;
    }

    public String getSenhaNota_lixeira() {
        return senhaNota_lixeira;
    }

    public void setSenhaNota_lixeira(String senhaNota_lixeira) {
        this.senhaNota_lixeira = senhaNota_lixeira;
    }

    public String getComAudio_lixeira() {
        return comAudio_lixeira;
    }

    public void setComAudio_lixeira(String comAudio_lixeira) {
        this.comAudio_lixeira = comAudio_lixeira;
    }

    public String getCaminhoAudio_lixeira() {
        return caminhoAudio_lixeira;
    }

    public void setCaminhoAudio_lixeira(String caminhoAudio_lixeira) {
        this.caminhoAudio_lixeira = caminhoAudio_lixeira;
    }

    public String getComImagem_lixeira() {
        return comImagem_lixeira;
    }

    public void setComImagem_lixeira(String comImagem_lixeira) {
        this.comImagem_lixeira = comImagem_lixeira;
    }

    public String getCaminhoImagem_lixeira() {
        return caminhoImagem_lixeira;
    }

    public void setCaminhoImagem_lixeira(String caminhoImagem_lixeira) {
        this.caminhoImagem_lixeira = caminhoImagem_lixeira;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
