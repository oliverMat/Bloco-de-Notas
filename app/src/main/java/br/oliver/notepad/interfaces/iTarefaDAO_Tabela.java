package br.oliver.notepad.interfaces;


import java.util.List;

import br.oliver.notepad.model.Tarefas_Tabela;

public interface iTarefaDAO_Tabela {

    boolean salvar(Tarefas_Tabela tarefas_tabela);
    boolean atualizar(Tarefas_Tabela tarefas_tabela);
    boolean deletar(Tarefas_Tabela tarefas_tabela);
    List<Tarefas_Tabela> listar();
    List<Tarefas_Tabela> listarNome(String nome);

}
