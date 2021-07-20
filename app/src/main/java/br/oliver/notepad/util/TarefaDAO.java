package br.oliver.notepad.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.oliver.notepad.interfaces.iTarefaDAO;
import br.oliver.notepad.model.Tarefa;

public class TarefaDAO implements iTarefaDAO {

    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;

    public TarefaDAO(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nomeTabela",tarefa.getNomeTabela());
        cv.put("nome",tarefa.getNomeTarefa());
        cv.put("descricao",tarefa.getNomeDescricao());
        cv.put("cor",tarefa.getCor());
        cv.put("dataCriacao",tarefa.getDataCriacao());
        cv.put("horaCriacao",tarefa.getHoraCriacao());
        cv.put("comSenha",tarefa.getComSenha());
        cv.put("senhaNota",tarefa.getSenhaNota());
        cv.put("comAudio",tarefa.getComAudio());
        cv.put("caminhoAudio",tarefa.getCaminhoAudio());
        cv.put("comImagem",tarefa.getComImagem());
        cv.put("caminhoImagem",tarefa.getCaminhoImagem());
        cv.put("favorito",tarefa.getFavorito());

        try{
            escreve.insert(DbHelper.TABELA_TAREFAS,null, cv);
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        //cv.put("nomeTabela",tarefa.getNomeTabela());
        cv.put("nome",tarefa.getNomeTarefa());
        cv.put("descricao",tarefa.getNomeDescricao());
        cv.put("cor",tarefa.getCor());
        cv.put("dataCriacao",tarefa.getDataCriacao());
        cv.put("horaCriacao",tarefa.getHoraCriacao());
        cv.put("comSenha",tarefa.getComSenha());
        cv.put("senhaNota",tarefa.getSenhaNota());
        cv.put("comAudio",tarefa.getComAudio());
        cv.put("caminhoAudio",tarefa.getCaminhoAudio());
        cv.put("comImagem",tarefa.getComImagem());
        cv.put("caminhoImagem",tarefa.getCaminhoImagem());
        cv.put("favorito",tarefa.getFavorito());

        try{
            String[]args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS,cv,"id = ? ",args);
            Log.i("INFO","Tarefa atualizada com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizarSenha(Tarefa tarefa) {

        ContentValues cv = new ContentValues();

        cv.put("comSenha",tarefa.getComSenha());
        cv.put("senhaNota",tarefa.getSenhaNota());


        try{
            String[]args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS,cv,"id = ? ",args);
            Log.i("INFO","Tarefa atualizada com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizarNomeTab(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nomeTabela",tarefa.getNomeTabela());


        try{
            String[]args = {tarefa.getId().toString()};
            escreve.update(DbHelper.TABELA_TAREFAS,cv,"id = ? ",args);
            Log.i("INFO","Tarefa atualizada com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizarColuna(Tarefa tarefa, String nomeTabela) {//atualiza todos q tenha o mesmo nome

        ContentValues cv = new ContentValues();
        cv.put("nomeTabela",tarefa.getNomeTabela());

        try{

            String[]args = {nomeTabela};

            escreve.update(DbHelper.TABELA_TAREFAS,cv,"nomeTabela = ?" ,args);

            Log.i("INFO","Tarefa atualizada com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {

        try{
            String[]args = {tarefa.getId().toString()};
            escreve.delete(DbHelper.TABELA_TAREFAS,"id = ? ",args);
            Log.i("INFO","Tarefa removida com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao remover tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Tarefa> listarTodos() {

        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " ;";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTabela = c.getString(c.getColumnIndex("nomeTabela"));
            String nome = c.getString(c.getColumnIndex("nome"));
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

            String favorito = c.getString(c.getColumnIndex("favorito"));


            tarefa.setId(id);
            tarefa.setNomeTabela(nomeTabela);
            tarefa.setNomeTarefa(nome);
            tarefa.setNomeDescricao(descricaoTarefa);
            tarefa.setCor(corTarefa);
            tarefa.setDataCriacao(DataCriacaoTarefa);
            tarefa.setHoraCriacao(HoraCriacao);
            tarefa.setComSenha(comSenha);
            tarefa.setSenhaNota(SenhaNota);
            tarefa.setComAudio(comAudio);
            tarefa.setCaminhoAudio(caminhoAudio);
            tarefa.setComImagem(comImagem);
            tarefa.setCaminhoImagem(caminhoImagem);
            tarefa.setFavorito(favorito);


            tarefas.add(tarefa);
        }

        c.close();

        return tarefas;
    }

    @Override
    public List<Tarefa> listarFavoritos() {

        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " WHERE favorito = " + "'" + "1" + "';";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTabela = c.getString(c.getColumnIndex("nomeTabela"));
            String nome = c.getString(c.getColumnIndex("nome"));
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

            String favorito = c.getString(c.getColumnIndex("favorito"));

            tarefa.setId(id);
            tarefa.setNomeTabela(nomeTabela);
            tarefa.setNomeTarefa(nome);
            tarefa.setNomeDescricao(descricaoTarefa);
            tarefa.setCor(corTarefa);
            tarefa.setDataCriacao(DataCriacaoTarefa);
            tarefa.setHoraCriacao(HoraCriacao);
            tarefa.setComSenha(comSenha);
            tarefa.setSenhaNota(SenhaNota);
            tarefa.setComAudio(comAudio);
            tarefa.setCaminhoAudio(caminhoAudio);
            tarefa.setComImagem(comImagem);
            tarefa.setCaminhoImagem(caminhoImagem);
            tarefa.setFavorito(favorito);


            tarefas.add(tarefa);
        }
        c.close();

        return tarefas;
    }

    @Override
    public List<Tarefa> listar(String nomeTab) {

        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " WHERE nomeTabela = " + "'" + nomeTab + "';";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTabela = c.getString(c.getColumnIndex("nomeTabela"));
            String nome = c.getString(c.getColumnIndex("nome"));
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

            String favorito = c.getString(c.getColumnIndex("favorito"));

            tarefa.setId(id);
            tarefa.setNomeTabela(nomeTabela);
            tarefa.setNomeTarefa(nome);
            tarefa.setNomeDescricao(descricaoTarefa);
            tarefa.setCor(corTarefa);
            tarefa.setDataCriacao(DataCriacaoTarefa);
            tarefa.setHoraCriacao(HoraCriacao);
            tarefa.setComSenha(comSenha);
            tarefa.setSenhaNota(SenhaNota);
            tarefa.setComAudio(comAudio);
            tarefa.setCaminhoAudio(caminhoAudio);
            tarefa.setComImagem(comImagem);
            tarefa.setCaminhoImagem(caminhoImagem);
            tarefa.setFavorito(favorito);


            tarefas.add(tarefa);
        }
        c.close();

        return tarefas;
    }

    @Override
    public List<Tarefa> pesquisaNotas(String nomePesquisa) {

        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " WHERE nome Like " + "'%" + nomePesquisa + "%';";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefa tarefa = new Tarefa();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTabela = c.getString(c.getColumnIndex("nomeTabela"));
            String nome = c.getString(c.getColumnIndex("nome"));
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

            String favorito = c.getString(c.getColumnIndex("favorito"));

            tarefa.setId(id);
            tarefa.setNomeTabela(nomeTabela);
            tarefa.setNomeTarefa(nome);
            tarefa.setNomeDescricao(descricaoTarefa);
            tarefa.setCor(corTarefa);
            tarefa.setDataCriacao(DataCriacaoTarefa);
            tarefa.setHoraCriacao(HoraCriacao);
            tarefa.setComSenha(comSenha);
            tarefa.setSenhaNota(SenhaNota);
            tarefa.setComAudio(comAudio);
            tarefa.setCaminhoAudio(caminhoAudio);
            tarefa.setComImagem(comImagem);
            tarefa.setCaminhoImagem(caminhoImagem);
            tarefa.setFavorito(favorito);


            tarefas.add(tarefa);
        }
        c.close();

        return tarefas;
    }


    @Override
    public List<Tarefa> listarTabela(String nomeTab) {//lista pasta da tabelaActivity verifica se existe notas na pasta

        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS + " WHERE nomeTabela = " + "'" + nomeTab + "';";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefa tarefa = new Tarefa();

            String nomeTabela = c.getString(c.getColumnIndex("nomeTabela"));

            tarefa.setNomeTabela(nomeTabela);

            tarefas.add(tarefa);
        }
        c.close();

        return tarefas;
    }
}
