package br.oliver.notepad.interfaces;


import java.util.List;

import br.oliver.notepad.model.Preferencia_Usuario;

public interface iPreferencia_Usuario {

    boolean salvar(Preferencia_Usuario preferencia_usuario);
    boolean atualizar(Preferencia_Usuario preferencia_usuario);
    boolean deletar(Preferencia_Usuario preferencia_usuario);
    List<Preferencia_Usuario> listarNome();

}
