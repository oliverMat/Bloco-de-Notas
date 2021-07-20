package br.oliver.notepad.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.oliver.notepad.interfaces.iTarefaDAO_Tabela;
import br.oliver.notepad.model.Tarefas_Tabela;

public class TarefaDAO_Tabela implements iTarefaDAO_Tabela {

    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;

    public TarefaDAO_Tabela(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefas_Tabela tarefas_tabela) {

        ContentValues cv = new ContentValues();
        cv.put("nomeTab", tarefas_tabela.getNomeTab());


        try{
            escreve.insert(DbHelper.TABELA_TAREFAS_TABELA,null, cv);
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefas_Tabela tarefas_tabela) {

        ContentValues cv = new ContentValues();
        cv.put("nomeTab", tarefas_tabela.getNomeTab());


        try{
            String[]args = {tarefas_tabela.getId_Tab().toString()};

            escreve.update(DbHelper.TABELA_TAREFAS_TABELA,cv,"id = ? ",args);
            Log.i("INFO","Tarefa atualizada com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Tarefas_Tabela tarefas_tabela) {

        try{
            String[]args = {tarefas_tabela.getId_Tab().toString()};
            escreve.delete(DbHelper.TABELA_TAREFAS_TABELA,"id = ? ",args);
            Log.i("INFO","Tarefa removida com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao remover tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Tarefas_Tabela> listar() {

        List<Tarefas_Tabela> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS_TABELA + " ;";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefas_Tabela tarefas_tabela = new Tarefas_Tabela();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTabela = c.getString(c.getColumnIndex("nomeTab"));

            tarefas_tabela.setId_Tab(id);
            tarefas_tabela.setNomeTab(nomeTabela);


            tarefas.add(tarefas_tabela);
        }

        c.close();

        return tarefas;
    }

    @Override
    public List<Tarefas_Tabela> listarNome(String nome) {

        List<Tarefas_Tabela> tarefas = new ArrayList<>();



        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFAS_TABELA + " WHERE nomeTab = " + "'" + nome + "';";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Tarefas_Tabela tarefas_tabela = new Tarefas_Tabela();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTabela = c.getString(c.getColumnIndex("nomeTab"));

            tarefas_tabela.setId_Tab(id);
            tarefas_tabela.setNomeTab(nomeTabela);


            tarefas.add(tarefas_tabela);
        }
        c.close();
        return tarefas;
    }
}
