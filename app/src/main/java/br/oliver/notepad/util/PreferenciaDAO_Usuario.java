package br.oliver.notepad.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import br.oliver.notepad.interfaces.iPreferencia_Usuario;
import br.oliver.notepad.model.Preferencia_Usuario;

public class PreferenciaDAO_Usuario implements iPreferencia_Usuario {

    private final SQLiteDatabase escreve;
    private final SQLiteDatabase le;

    public PreferenciaDAO_Usuario(Context context) {
        DbHelper db = new DbHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Preferencia_Usuario preferencia_usuario) {

        ContentValues cv = new ContentValues();
        cv.put("corPrim", preferencia_usuario.getCorPrim());
        cv.put("comSensor", preferencia_usuario.getAutoPlay());
        cv.put("premium", preferencia_usuario.getPremium());
        cv.put("exibirQuestionario", preferencia_usuario.getExibirQuesti());


        try{
            escreve.insert(DbHelper.TABELA_PREFERENCIAS,null, cv);
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Preferencia_Usuario preferencia_usuario) {

        ContentValues cv = new ContentValues();
        cv.put("corPrim", preferencia_usuario.getCorPrim());
        cv.put("comSensor", preferencia_usuario.getAutoPlay());
        cv.put("premium", preferencia_usuario.getPremium());
        cv.put("exibirQuestionario", preferencia_usuario.getExibirQuesti());


        try{
            String[]args = {preferencia_usuario.getId().toString()};

            escreve.update(DbHelper.TABELA_PREFERENCIAS,cv,"id = ? ",args);
            Log.i("INFO","Tarefa atualizada com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao salvar tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Preferencia_Usuario preferencia_usuario) {

        try{
            String where = "id IS NOT NULL";

            escreve.delete(DbHelper.TABELA_PREFERENCIAS, where, null);
            Log.i("INFO","Tarefa removida com sucesso!");
        }catch (Exception e ){
            Log.e("INFO","Erro ao remover tarefa "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public List<Preferencia_Usuario> listarNome() {

        List<Preferencia_Usuario> tarefas = new ArrayList<>();



        String sql = "SELECT * FROM " + DbHelper.TABELA_PREFERENCIAS + ";";
        Cursor c = le.rawQuery(sql,null);

        while (c.moveToNext()){

            Preferencia_Usuario preferencia_usuario = new Preferencia_Usuario();

            Long id = c.getLong(c.getColumnIndex("id"));
            String nomeTabela = c.getString(c.getColumnIndex("corPrim"));
            String comSensor = c.getString(c.getColumnIndex("comSensor"));
            String premium = c.getString(c.getColumnIndex("premium"));
            String exibirQuesti = c.getString(c.getColumnIndex("exibirQuestionario"));

            preferencia_usuario.setId(id);
            preferencia_usuario.setCorPrim(nomeTabela);
            preferencia_usuario.setAutoPlay(comSensor);
            preferencia_usuario.setPremium(premium);
            preferencia_usuario.setExibirQuesti(exibirQuesti);


            tarefas.add(preferencia_usuario);
        }

        c.close();

        return tarefas;
    }
}
