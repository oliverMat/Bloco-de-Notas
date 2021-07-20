package br.oliver.notepad.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION = 5;
    public static String NOME_DB = "DB_TAREFAS";
    public static String TABELA_TAREFAS = "tarefas";
    public static String TABELA_TAREFAS_LIXEIRA = "tarefas_lixeira";
    public static String TABELA_TAREFAS_TABELA = "tarefas_tabela";
    public static String TABELA_PREFERENCIAS = "preferencia_usuario";

    public DbHelper(Context context){
        super(context, NOME_DB, null, VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS +" (id INTEGER PRIMARY KEY AUTOINCREMENT, " + " nomeTabela TEXT NOT NULL, " + " nome TEXT NOT NULL, " + " descricao VARCHAR , " + " cor VARCHAR NOT NULL , " + " dataCriacao TEXT NOT NULL , " + " horaCriacao TEXT NOT NULL , " + " comSenha TEXT NOT NULL , " + " senhaNota  TEXT , " + " comAudio TEXT NOT NULL , " + " caminhoAudio TEXT , " + " comImagem TEXT NOT NULL , " + " caminhoImagem TEXT, "+ " favorito TEXT NOT NULL ); ";

        String sql_lixeira = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS_LIXEIRA +" (id INTEGER PRIMARY KEY AUTOINCREMENT, " + " nomeTabela TEXT NOT NULL, " + " nome TEXT NOT NULL, " + " descricao VARCHAR , " + " cor VARCHAR NOT NULL , " + " dataCriacao TEXT NOT NULL , " + " horaCriacao TEXT NOT NULL , " + " comSenha TEXT NOT NULL , " + " senhaNota  TEXT , " + " comAudio TEXT NOT NULL , " + " caminhoAudio TEXT , " + " comImagem TEXT NOT NULL , " + " caminhoImagem TEXT ); ";

        String sql_tabela = "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFAS_TABELA +" (id INTEGER PRIMARY KEY AUTOINCREMENT, " + " nomeTab TEXT NOT NULL ); ";


        String sql_preferencia_usuario = "CREATE TABLE IF NOT EXISTS " + TABELA_PREFERENCIAS +" (id INTEGER PRIMARY KEY , " + " corPrim TEXT NOT NULL , " + " comSensor TEXT NOT NULL , " + " premium TEXT NOT NULL , " + " exibirQuestionario TEXT NOT NULL); ";



        try {
            db.execSQL(sql);
            db.execSQL(sql_lixeira);
            db.execSQL(sql_tabela);
            db.execSQL(sql_preferencia_usuario);
            Log.i("INFO DB","Sucesso ao criar a tabela");
        }catch (Exception e ){
            Log.i("INFO DB","Erro ao criar a tabela"+ e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        String sql_preferencia_usuario2 = "ALTER TABLE " + "preferencia_usuario" + " ADD COLUMN" + " exibirQuestionario TEXT NOT NULL DEFAULT 0 ;";//implementado na versao 2.0.6


        String sql_tarefa = "ALTER TABLE " + "tarefas" + " ADD COLUMN" + " favorito TEXT NOT NULL DEFAULT 0 ;";
        String sql_preferencia_usuario = "ALTER TABLE " + "preferencia_usuario" + " ADD COLUMN" + " comSensor TEXT NOT NULL DEFAULT 0 ;";
        String sql_preferencia_usuario1 = "ALTER TABLE " + "preferencia_usuario" + " ADD COLUMN" + " premium TEXT NOT NULL DEFAULT 0 ;";


        try {
            db.execSQL(sql_preferencia_usuario2);
            db.execSQL(sql_tarefa);
            db.execSQL(sql_preferencia_usuario);
            db.execSQL(sql_preferencia_usuario1);
            Log.i("INFO DB","Sucesso ao atualizar App");
        }catch (Exception e ){
            Log.i("INFO DB","Erro ao atualizar App"+ e.getMessage());
        }


    }
}
