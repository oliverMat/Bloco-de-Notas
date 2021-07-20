package br.oliver.notepad.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.oliver.notepad.interfaces.iTarefaDAO_Lixeira;
import br.oliver.notepad.model.Tarefa_Lixeira;

public class TarefaDAO_Lixeira implements iTarefaDAO_Lixeira {

    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;

    public TarefaDAO_Lixeira(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa_Lixeira tarefa_lixeira) {

        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa_lixeira.getNomeTarefa_lixeira());
        cv.put("nomeTabela", tarefa_lixeira.getNomeTabela_lixeira());
        cv.put("descricao", tarefa_lixeira.getNomeDescricao_lixeira());
        cv.put("cor", tarefa_lixeira.getCor_lixeira());
        cv.put("dataCriacao", tarefa_lixeira.getDataCriacao_lixeira());
        cv.put("horaCriacao", tarefa_lixeira.getHoraCriacao_lixeira());
        cv.put("comSenha", tarefa_lixeira.getComSenha_lixeira());
        cv.put("senhaNota", tarefa_lixeira.getSenhaNota_lixeira());
        cv.put("comAudio",tarefa_lixeira.getComAudio_lixeira());
        cv.put("caminhoAudio",tarefa_lixeira.getCaminhoAudio_lixeira());
        cv.put("comImagem",tarefa_lixeira.getComImagem_lixeira());
        cv.put("caminhoImagem",tarefa_lixeira.getCaminhoImagem_lixeira());

        try{
            escreve.insert(DbHelper.TABELA_TAREFAS_LIXEIRA,null, cv);
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa_Lixeira tarefa_lixeira) {

        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa_lixeira.getNomeTarefa_lixeira());
        cv.put("nomeTabela", tarefa_lixeira.getNomeTabela_lixeira());
        cv.put("descricao", tarefa_lixeira.getNomeDescricao_lixeira());
        cv.put("cor", tarefa_lixeira.getCor_lixeira());
        cv.put("dataCriacao", tarefa_lixeira.getDataCriacao_lixeira());
        cv.put("horaCriacao", tarefa_lixeira.getHoraCriacao_lixeira());
        cv.put("comSenha", tarefa_lixeira.getComSenha_lixeira());
        cv.put("senhaNota", tarefa_lixeira.getSenhaNota_lixeira());
        cv.put("comAudio",tarefa_lixeira.getComAudio_lixeira());
        cv.put("caminhoAudio",tarefa_lixeira.getCaminhoAudio_lixeira());
        cv.put("comImagem",tarefa_lixeira.getComImagem_lixeira());
        cv.put("caminhoImagem",tarefa_lixeira.getCaminhoImagem_lixeira());

        try{
            String[]args = {tarefa_lixeira.getId_lixeira().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS_LIXEIRA,cv,"id = ? ",args);
            Log.i("INFO","Tarefa atualizada com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Tarefa_Lixeira tarefa_lixeira) {

        try{
            String[]args = {tarefa_lixeira.getId_lixeira().toString()};
            escreve.delete(DbHelper.TABELA_TAREFAS_LIXEIRA,"id = ? ",args);
            Log.i("INFO","Tarefa removida com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao remover tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Tarefa_Lixeira> listar() {

        List<Tarefa_Lixeira> tarefasLixeirass = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS_LIXEIRA + " ;";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefa_Lixeira tarefa_lixeira = new Tarefa_Lixeira();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTarefa = c.getString(c.getColumnIndex("nome"));
            String nomeTabela = c.getString(c.getColumnIndex("nomeTabela"));
            String descricaoTarefa = c.getString(c.getColumnIndex("descricao"));
            String corTarefa = c.getString(c.getColumnIndex("cor"));
            String DataCriacaoTarefa = c.getString(c.getColumnIndex("dataCriacao"));
            String HoraCriacao = c.getString(c.getColumnIndex("horaCriacao"));
            String comSenha = c.getString(c.getColumnIndex("comSenha"));
            String SenhaNota = c.getString(c.getColumnIndex("senhaNota"));
            String comAudio = c.getString(c.getColumnIndex("comAudio"));
            String caminhoAudio = c.getString(c.getColumnIndex("caminhoAudio"));
            String comImagem = c.getString(c.getColumnIndex("comImagem"));
            String caminhoImagem = c.getString(c.getColumnIndex("caminhoImagem"));


            tarefa_lixeira.setId_lixeira(id);
            tarefa_lixeira.setNomeTarefa_lixeira(nomeTarefa);
            tarefa_lixeira.setNomeTabela_lixeira(nomeTabela);
            tarefa_lixeira.setNomeDescricao_lixeira(descricaoTarefa);
            tarefa_lixeira.setCor_lixeira(corTarefa);
            tarefa_lixeira.setDataCriacao_lixeira(DataCriacaoTarefa);
            tarefa_lixeira.setHoraCriacao_lixeira(HoraCriacao);
            tarefa_lixeira.setComSenha_lixeira(comSenha);
            tarefa_lixeira.setSenhaNota_lixeira(SenhaNota);
            tarefa_lixeira.setComAudio_lixeira(comAudio);
            tarefa_lixeira.setCaminhoAudio_lixeira(caminhoAudio);
            tarefa_lixeira.setComImagem_lixeira(comImagem);
            tarefa_lixeira.setCaminhoImagem_lixeira(caminhoImagem);


            tarefasLixeirass.add(tarefa_lixeira);
        }
        c.close();
        return tarefasLixeirass;
    }
}
