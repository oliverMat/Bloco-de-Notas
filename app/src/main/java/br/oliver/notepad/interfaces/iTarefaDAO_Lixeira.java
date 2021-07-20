package br.oliver.notepad.interfaces;


import java.util.List;

import br.oliver.notepad.model.Tarefa_Lixeira;

public interface iTarefaDAO_Lixeira {

    boolean salvar(Tarefa_Lixeira tarefa_lixeira);
    boolean atualizar(Tarefa_Lixeira tarefa_lixeira);
    boolean deletar(Tarefa_Lixeira tarefa_lixeira);
    List<Tarefa_Lixeira> listar();

}
