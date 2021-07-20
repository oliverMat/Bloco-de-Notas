package br.oliver.notepad.model;

import java.io.Serializable;

public class Preferencia_Usuario implements Serializable {

    private Long id;
    private String corPrim;
    private String autoPlay;
    private String premium;
    private String exibirQuesti;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorPrim() {
        return corPrim;
    }

    public void setCorPrim(String corPrim) {
        this.corPrim = corPrim;
    }

    public String getAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(String autoPlay) {
        this.autoPlay = autoPlay;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getExibirQuesti() {
        return exibirQuesti;
    }

    public void setExibirQuesti(String exibirQuesti) {
        this.exibirQuesti = exibirQuesti;
    }
}
