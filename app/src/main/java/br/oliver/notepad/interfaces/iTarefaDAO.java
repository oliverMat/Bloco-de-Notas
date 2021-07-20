package br.oliver.notepad.interfaces;


import java.util.List;

import br.oliver.notepad.model.Tarefa;

public interface iTarefaDAO {

    boolean salvar(Tarefa tarefa);
    boolean atualizar(Tarefa tarefa);
    boolean atualizarSenha(Tarefa tarefa);
    boolean atualizarNomeTab(Tarefa tarefa);
    boolean atualizarColuna(Tarefa tarefa, String nomeTabela);
    boolean deletar(Tarefa tarefa);

    List<Tarefa> listarTodos();
    List<Tarefa> listarFavoritos();
    List<Tarefa> listar(String nomeTabela);
    List<Tarefa> listarTabela(String nomeTabela);
    List<Tarefa> pesquisaNotas(String nomePesquisa);

}
