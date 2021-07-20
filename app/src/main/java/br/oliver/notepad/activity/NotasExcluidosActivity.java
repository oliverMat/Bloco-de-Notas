package br.oliver.notepad.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ContextWrapper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import br.oliver.notepad.R;
import br.oliver.notepad.adapter.NotasExcluidosAdapter;
import br.oliver.notepad.model.Tarefa_Lixeira;
import br.oliver.notepad.util.Grid_Largura;
import br.oliver.notepad.util.TarefaDAO_Lixeira;

public class NotasExcluidosActivity extends AppCompatActivity{

    private NotasExcluidosAdapter notasExcluidosAdapter;

    private FloatingActionButton fab_deletar;
    private AlertDialog dialogP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_excluidas);

        Toolbar toolbar = findViewById(R.id.toolbar_lixeira);
        toolbar.setTitle(R.string.lixeira);
        setSupportActionBar(toolbar);

        //botao de voltar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));//altera a cor dos botoes virtuais

        fab_deletar = findViewById(R.id.fab_deletar);
        carregar();
        excluir();

    }

    @Override
    public void onResume(){

        carregar();

        super.onResume();
    }

    public void carregar() {

        //listar tarefas
        TarefaDAO_Lixeira tarefaDAOConcluido = new TarefaDAO_Lixeira(getApplicationContext());
        List<Tarefa_Lixeira> listaTarefasConcluidas = tarefaDAOConcluido.listar();
        //Configurando recycler
        RecyclerView recyclerView_concluido = findViewById(R.id.recyclerListaTarefa_Concluido);
        /*
            Exibir lista de tarefas no RecyclerView
         */
        //Configurando um adapter
        notasExcluidosAdapter = new NotasExcluidosAdapter(listaTarefasConcluidas);

        int mNoOfColumns = Grid_Largura.calculateNoOfColumns(getApplicationContext(),180);//faz com que os blocos se ajustem automaticamente

        //configurando recyclerview
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(mNoOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerView_concluido.setLayoutManager(layoutManager);
        recyclerView_concluido.setHasFixedSize(true);
        Collections.reverse(listaTarefasConcluidas);
        recyclerView_concluido.setAdapter(notasExcluidosAdapter);

        TextView tv_concluidos = findViewById(R.id.tv_concluidos);
        ImageView check_concl = findViewById(R.id.check_concl);
        fab_deletar = findViewById(R.id.fab_deletar);
        fab_deletar.hide();

        if (listaTarefasConcluidas.isEmpty()) {

            tv_concluidos.setVisibility(View.VISIBLE);
            check_concl.setVisibility(View.VISIBLE);

        } else {

            tv_concluidos.setVisibility(View.INVISIBLE);
            check_concl.setVisibility(View.INVISIBLE);
        }

        notasExcluidosAdapter.setListener(new NotasExcluidosAdapter.LixeiraAdapterListener() {//verifica se o item esta selecionado
            @Override
            public void onItemClick(int position) {
                enableActionMode(position);
            }

            @Override
            public void onItemLongClick(int position) {
                enableActionMode(position);

                Vibrator rr = (Vibrator) getSystemService(ContextWrapper.VIBRATOR_SERVICE);
                long milliseconds = 30;
                rr.vibrate(milliseconds);
            }
        });

    }

    private void enableActionMode(int position) {

        notasExcluidosAdapter.toggleSelection(position);
        final int size = notasExcluidosAdapter.selectedItems.size();

        if (size == 0) {//actionMode.finish();
            fab_deletar.hide();
        } else {
            fab_deletar.show();
        }

    }

    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();  // optional depending on your needs
    }

    public void excluir(){

        try {

            fab_deletar = findViewById(R.id.fab_deletar);

            fab_deletar.setOnClickListener(v -> {

                int size = notasExcluidosAdapter.selectedItems.size();

                AlertDialog.Builder dialog = new AlertDialog.Builder(NotasExcluidosActivity.this, R.style.DialogStyle);
                //Configurando titulo e mensagem
                dialog.setTitle(R.string.excluir);
                dialog.setMessage(getResources().getString(R.string.deseJaExcluir) + " "+ size +" ?" );

                //exclui a pasta
                dialog.setPositiveButton(R.string.sim, (dialog1, which) -> {//exclui todos os item selecionados , vem do adapter

                    for (int i = 0; i < notasExcluidosAdapter.selectedItems.size(); i++) {

                        if (notasExcluidosAdapter.getSelected().get(i).getComAudio_lixeira().equals("1")){

                            excluirAudio(notasExcluidosAdapter.getSelected().get(i).getCaminhoAudio_lixeira());

                        }else if (notasExcluidosAdapter.getSelected().get(i).getComImagem_lixeira().equals("1")){

                            excluirImg(notasExcluidosAdapter.getSelected().get(i).getCaminhoImagem_lixeira());
                        }

                        TarefaDAO_Lixeira tarefaDAOLixeira =new TarefaDAO_Lixeira(getApplicationContext());
                        if ((!tarefaDAOLixeira.deletar(notasExcluidosAdapter.getSelected().get(i)))) {
                            //finish();
                            //Toast.makeText(getApplicationContext(),"Excluida!",Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getApplicationContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();

                            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_bottom));
                            snack.show();
                        }


                    }

                    carregar();

                });
                dialog.setNegativeButton(R.string.nao, null);

                //Exibir a dialog
                dialogP = dialog.create();

                dialogP.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogP.show();

            });

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void excluirAudio(String audio) {

        try {
            //path = "caminho da pasta/arquivo, etc";

            boolean p = new File(audio).delete();

            if(p){

                //Toast.makeText(NotasExcluidosActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();

            }else{

                //Toast.makeText(NotasExcluidosActivity.this, "Erro audio", Toast.LENGTH_SHORT).show();

            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void excluirImg(String imag) {
        try {
            //path = "caminho da pasta/arquivo, etc";
            boolean p = new File(imag).delete();

            if (p) {

                //Toast.makeText(EditarImagemActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();

            } else {

                //Toast.makeText(NotasExcluidosActivity.this, "Erro imag", Toast.LENGTH_SHORT).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}