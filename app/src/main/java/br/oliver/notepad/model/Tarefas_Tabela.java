package br.oliver.notepad.model;

import java.io.Serializable;

public class Tarefas_Tabela implements Serializable {

    private Long id_Tab;
    private String nomeTab;

    private boolean selected;


    public Long getId_Tab() {
        return id_Tab;
    }

    public void setId_Tab(Long id_Tab) {
        this.id_Tab = id_Tab;
    }

    public String getNomeTab() {
        return nomeTab;
    }

    public void setNomeTab(String nomeTab) {
        this.nomeTab = nomeTab;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
