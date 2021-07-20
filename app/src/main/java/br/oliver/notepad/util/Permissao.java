package br.oliver.notepad.util;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validarPermissoes(String[] permissoes, Activity activity , int requestCode){

        List<String> listaPermissoes = new ArrayList<>();

            /*Percorre as permissoes passadas,
            verifica uma uma * se ja tem permissao liberada */
        for (String permissao : permissoes){
          boolean temPermisao = ContextCompat.checkSelfPermission(activity , permissao) == PackageManager.PERMISSION_GRANTED;
          if(!temPermisao) listaPermissoes.add(permissao);
        }
        // caso a lista esteja vaiza , nae e necessario solicitar permissaoes
        if (listaPermissoes.isEmpty()) return true;
        String[] novasPermissoes = new String[listaPermissoes.size()];
        listaPermissoes.toArray(novasPermissoes);

        //Solicita permissao
        ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode );

        return true;
    }
}
