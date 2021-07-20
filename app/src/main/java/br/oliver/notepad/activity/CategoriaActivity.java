package br.oliver.notepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.ValueAnimator;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.roacult.backdrop.BackdropLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.oliver.notepad.R;
import br.oliver.notepad.adapter.NotaAdapter;
import br.oliver.notepad.adapter.NotaCategoriaAdapter;
import br.oliver.notepad.model.Preferencia_Usuario;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.model.Tarefas_Tabela;
import br.oliver.notepad.util.PreferenciaDAO_Usuario;
import br.oliver.notepad.util.RecyclerItemClickListener;
import br.oliver.notepad.util.TarefaDAO;
import br.oliver.notepad.util.TarefaDAO_Tabela;
import br.oliver.notepad.util.Grid_Largura;


public class CategoriaActivity extends AppCompatActivity {

     /* This project was made to help the user and keep him organized.

     Say there blz, any doubts you can walk an email: oliverdev22@gmail.com  */


    private RecyclerView recycler_Categoria, recycler_Tarefas;
    private List<Tarefas_Tabela> listaTarefas_tabelas = new ArrayList<>();
    private List<Tarefas_Tabela> listaTarefas_nome = new ArrayList<>();
    private Tarefas_Tabela tabelaSelecionada;
    private NotaCategoriaAdapter notaCategoriaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();

    private AlertDialog dialog, dialogRename;
    private Menu menu;

    private String nomeTabela;
    private boolean backIcon = false, iconSearch = false;
    private int UltimapositCat;

    private BackdropLayout backdropContainer;
    private ValueAnimator valueAnimator, ValueAnimatorSearch;
    private SearchView searchViewPesquisa;
    private ImageButton action_serach, bt_exibirTodos, bt_Favorito;
    private LinearLayout ocultarPesquisa, acoesCategoria;
    private TextView tv_nome_Tab, tv_Toolbar;
    private ImageView iv_iconVazio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        tv_nome_Tab = findViewById(R.id.tv_nome_Tab);

        carregar();
        carregarCategoria();
        carregarTarefas("Note",null);
        criarMinhaNota();
        escolherAcao();
        serchView();
        criarPreferencia();

        exibirAvaliacao();

    }

    private void carregar(){

        Toolbar toolbar = findViewById(R.id.toolbar_Categoria);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        backdropContainer = findViewById(R.id.backdropcontainer);
        tv_Toolbar = findViewById(R.id.tv_Toolbar);
        bt_exibirTodos = findViewById(R.id.bt_exibirTodos);
        bt_Favorito = findViewById(R.id.bt_Favorito);
        iv_iconVazio = findViewById(R.id.iv_iconVazio);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));//altera a cor dos botoes virtuais


        //-----------------------------------------------------------------------------------------------


        valueAnimator = ValueAnimator.ofFloat(0f, 2f);
        valueAnimator.setDuration(700);
        valueAnimator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            tv_Toolbar.setAlpha(alpha);
        });
        valueAnimator.start();

        backdropContainer.setOnBackdropChangeStateListener(state -> {

            if (state == BackdropLayout.State.OPEN){

                try {

                    tv_Toolbar.setText(R.string.categoria);
                    acoesCategoria.animate().translationY(0);
                    backIcon = true;
                    valueAnimator.start();

                    if (ocultarPesquisa.getVisibility() == View.VISIBLE) {
                        //fechar searchView
                        searchViewPesquisa.onActionViewCollapsed();
                        ocultarPesquisa.setVisibility(View.GONE);
                        tv_nome_Tab.setVisibility(View.VISIBLE);
                        ValueAnimatorSearch.start();
                        action_serach.setBackgroundResource(R.drawable.ic_search);
                        iconSearch = false;
                    }

                    menu.getItem(0).setVisible(true);
                    menu.getItem(1).setVisible(false);
                    menu.getItem(2).setVisible(false);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }else if (state == BackdropLayout.State.CLOSE){

                try {

                    valueAnimator.start();
                    tv_Toolbar.setText("");
                    backIcon = false;
                    // carregarTarefas();

                    menu.getItem(0).setVisible(false);
                    menu.getItem(1).setVisible(true);
                    menu.getItem(2).setVisible(true);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }


            return null;
        });

        findViewById(R.id.close_lista).setOnClickListener(v -> backdropContainer.close());

        bt_exibirTodos.setOnClickListener(v -> {

            backdropContainer.close();
            carregarCategoria();
            notaCategoriaAdapter.cliclado(1000);
            bt_exibirTodos.setBackgroundResource(R.drawable.fundo_redondo_cinza);
            bt_Favorito.setBackgroundResource(R.drawable.fundo_redondo_preto);


            new Handler(Looper.getMainLooper()).postDelayed(() -> carregarTarefas("t",null), 450);

        });

        bt_Favorito.setOnClickListener(v -> {

            backdropContainer.close();
            carregarCategoria();
            notaCategoriaAdapter.cliclado(1000);
            bt_exibirTodos.setBackgroundResource(R.drawable.fundo_redondo_preto);
            bt_Favorito.setBackgroundResource(R.drawable.fundo_redondo_cinza);


            new Handler(Looper.getMainLooper()).postDelayed(() -> carregarTarefas("f",null), 450);
        });


        //---------------------------------RECYCLER-VIEW----------------------------------------------


        //Configurando recycler
        recycler_Tarefas = findViewById(R.id.recyclerListaTarefa);
        recycler_Categoria = findViewById(R.id.recycler_Categoria);
        acoesCategoria = findViewById(R.id.acoesCategoria);

        recycler_Tarefas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 ) {
                    acoesCategoria.animate().translationY(acoesCategoria.getHeight() + 100);
                } else if (dy < 0 ) {
                    acoesCategoria.animate().translationY(0);
                }
            }});


        recycler_Categoria.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recycler_Categoria, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                //Recuperando tarefa para edicao
                Tarefas_Tabela tabelaSelecionada = listaTarefas_tabelas.get(position);

                nomeTabela = tabelaSelecionada.getNomeTab();

                carregarCategoria();
                backdropContainer.close();
                notaCategoriaAdapter.cliclado(position);
                UltimapositCat = position;

                bt_exibirTodos.setBackgroundResource(R.drawable.fundo_redondo_preto);
                bt_Favorito.setBackgroundResource(R.drawable.fundo_redondo_preto);


                new Handler(Looper.getMainLooper()).postDelayed(() -> carregarTarefas(tabelaSelecionada.getNomeTab(),null), 450);

            }


            @Override
            public void onLongItemClick(View view, int position) {//verifica se a pasta esta vazia ou cheia

                Tarefas_Tabela tabela = listaTarefas_tabelas.get(position);

                carregarCategoria();
                notaCategoriaAdapter.cliclado(position);
                UltimapositCat = position;
                carregarTarefas(tabela.getNomeTab(),null);

                try {

                    Vibrator rr = (Vibrator) getSystemService(ContextWrapper.VIBRATOR_SERVICE);
                    long milliseconds = 30;
                    rr.vibrate(milliseconds);

                    tabelaSelecionada = listaTarefas_tabelas.get(position);


                    if (tabelaSelecionada.getNomeTab().equals("Note")){

                        AlertDialog.Builder dialogInterno = new AlertDialog.Builder(CategoriaActivity.this, R.style.DialogStyle);
                        //Configurando titulo e mensagem
                        //dialogInterno.setTitle("Categoria padrao nao pode ser alterada");
                        dialogInterno.setMessage(R.string.mensagemPadrao);

                        dialogInterno.setPositiveButton("Ok", (dialog, which) -> {

                        });

                        //Exibir a dialog
                        dialog = dialogInterno.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }else {

                        AlertDialog.Builder dialog = new AlertDialog.Builder(CategoriaActivity.this, R.style.DialogStyle);
                        //Configurando titulo e mensagem
                        //dialog.setTitle("Deseja excluir : " + tabelaSelecionada.getNomeTab() + " ?");
                        dialog.setMessage(getResources().getString(R.string.deseJaExcluir)+ " " + tabelaSelecionada.getNomeTab() + " ?");


                        dialog.setNeutralButton(R.string.renomear, (dialog12, which) -> {//renomeia a pasta

                            final View cusntom = getLayoutInflater().inflate(R.layout.edit_text_tabela_dialog, null);

                            AlertDialog.Builder dialogInterno = new AlertDialog.Builder(CategoriaActivity.this, R.style.DialogStyle);
                            //Configurando titulo e mensagem
                            dialogInterno.setView(cusntom);
                            //dialogInterno.setTitle("");
                            dialogInterno.setMessage(R.string.renomeieAsuaCategoria);

                            dialogInterno.setPositiveButton(R.string.alterar, (dialog121, which1) -> {

                                EditText editText = cusntom.findViewById(R.id.ed_Tabela_Dialog);

                                String nomeTedit = editText.getText().toString();

                                //Executar acao para salvar item no DB
                                TarefaDAO_Tabela tarefaDAO_tabela = new TarefaDAO_Tabela(getApplicationContext());

                                if ( !nomeTedit.isEmpty()) {

                                    listaTarefas_nome = tarefaDAO_tabela.listarNome(nomeTedit);

                                    if (listaTarefas_nome.isEmpty()){//Verifica se a categoria existe


                                        Tarefas_Tabela tarefas_tabela = new Tarefas_Tabela();
                                        tarefas_tabela.setId_Tab(tabelaSelecionada.getId_Tab());
                                        tarefas_tabela.setNomeTab(nomeTedit);


                                        if (tarefaDAO_tabela.atualizar(tarefas_tabela)) {

                                            TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                                            Tarefa tarefa = new Tarefa();

                                            tarefa.setNomeTabela(nomeTedit);

                                            if(tarefaDAO.atualizarColuna(tarefa, tabelaSelecionada.getNomeTab())){

                                                carregarCategoria();
                                                //nomeTabela = nomeTedit;
                                                carregarTarefas(nomeTedit,null);

                                            }

                                            //Toast.makeText(getApplicationContext(),R.string.atualizado,Toast.LENGTH_SHORT).show();
                                            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.atualizado, Snackbar.LENGTH_SHORT);
                                            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                                            snack.show();

                                        } else {

                                            //Toast.makeText(getApplicationContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();
                                            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                                            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                                            snack.show();

                                        }

                                    }else {

                                        //Toast.makeText(getApplicationContext(), R.string.categoriaJaExiste, Toast.LENGTH_SHORT).show();
                                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.categoriaJaExiste, Snackbar.LENGTH_SHORT);
                                        snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                                        snack.show();
                                    }

                                }else {

                                    //Toast.makeText(getApplicationContext(),R.string.nadaAqui,Toast.LENGTH_SHORT).show();
                                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.nadaAqui, Snackbar.LENGTH_SHORT);
                                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                                    snack.show();

                                }
                            });
                            dialogInterno.setNegativeButton(R.string.cancelar, null);

                            //Exibir a dialog
                            dialogRename = dialogInterno.create();
                            dialogRename.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialogRename.show();
                        });

                        //exclui a pasta
                        dialog.setPositiveButton(R.string.sim, (dialog1, which) -> {

                            TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                            listaTarefas = tarefaDAO.listarTabela(tabelaSelecionada.getNomeTab());

                            if (listaTarefas.isEmpty()) {// verifica se e a categoria padrao para ser excluida

                                TarefaDAO_Tabela tarefaDAO_tabela = new TarefaDAO_Tabela(getApplicationContext());
                                if (tarefaDAO_tabela.deletar(tabelaSelecionada)) {

                                    UltimapositCat = 0;
                                    //nomeTabela = "Note";
                                    carregarCategoria();
                                    carregarTarefas("Note",null);

                                    //Toast.makeText(getApplicationContext(),R.string.excluido,Toast.LENGTH_SHORT).show();
                                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.excluido, Snackbar.LENGTH_SHORT);
                                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                                    snack.show();

                                } else {
                                    //Toast.makeText(getApplicationContext(), R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();
                                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                                    snack.show();
                                }

                            } else {

                                //Toast.makeText(getApplicationContext(),R.string.categoriaCheiaNaoPodeSerExcluida,Toast.LENGTH_SHORT).show();
                                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.categoriaCheiaNaoPodeSerExcluida, Snackbar.LENGTH_SHORT);
                                snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                                snack.show();

                            }
                        });
                        dialog.setNegativeButton(R.string.nao, null);

                        //Exibir a dialog
                        dialogRename = dialog.create();
                        dialogRename.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialogRename.show();

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        }));

    }

    private void serchView(){

        searchViewPesquisa = findViewById(R.id.searchViewPesquisa);
        action_serach = findViewById(R.id.action_serach);
        ocultarPesquisa = findViewById(R.id.ocultarPesquisa);

        ValueAnimatorSearch = ValueAnimator.ofFloat(0f, 2f);
        ValueAnimatorSearch.setDuration(500);
        ValueAnimatorSearch.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            tv_nome_Tab.setAlpha(alpha);
            ocultarPesquisa.setAlpha(alpha);
            action_serach.setAlpha(alpha);
        });


        action_serach.setOnClickListener(v -> {

            if (backIcon){

                backdropContainer.close();

            }

            if(!iconSearch ){
                ocultarPesquisa.setVisibility(View.VISIBLE);
                tv_nome_Tab.setVisibility(View.GONE);
                action_serach.setBackgroundResource(R.drawable.ic_close_black);
                searchViewPesquisa.setIconifiedByDefault(true);
                searchViewPesquisa.setFocusable(true);
                searchViewPesquisa.setIconified(false);
                searchViewPesquisa.requestFocusFromTouch();
                acoesCategoria.animate().translationY(acoesCategoria.getHeight() + 100);
                bt_exibirTodos.setBackgroundResource(R.drawable.fundo_redondo_preto);
                bt_Favorito.setBackgroundResource(R.drawable.fundo_redondo_preto);

                ValueAnimatorSearch.start();

                iconSearch = true;

            }else {

                searchViewPesquisa.onActionViewCollapsed();
                ocultarPesquisa.setVisibility(View.GONE);
                tv_nome_Tab.setVisibility(View.VISIBLE);
                ValueAnimatorSearch.start();
                action_serach.setBackgroundResource(R.drawable.ic_search);
                iconSearch = false;
                acoesCategoria.animate().translationY(0);
                carregarTarefas("Note", null);
            }

        });

        //Confugurando searchview
        //searchViewPesquisa.setQueryHint(R.string.buscarNotas);
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String textoDigitado = newText.toLowerCase();
                //pesquisaNota(textoDigitado);

                if(newText.length()>=1) {

                    carregarTarefas("p",textoDigitado);

                }else {
                    carregarTarefas("Note",null);
                    carregarCategoria();
                }
                return true;
            }
        });

    }

    private void carregarTarefas(String n, String pesqui){

        try {

            //listar tarefas
            TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
            List<Tarefa> listaTarefa;
            NotaAdapter notaAdapter;

            switch (n) {
                case "f":

                    listaTarefa = tarefaDAO.listarFavoritos();
                    notaAdapter = new NotaAdapter(listaTarefa);
                    tv_nome_Tab.setText(R.string.favoritos);

                    break;
                case "t":

                    listaTarefa = tarefaDAO.listarTodos();
                    notaAdapter = new NotaAdapter(listaTarefa);
                    tv_nome_Tab.setText(R.string.todasNotas);

                    break;
                case "p":

                    listaTarefa = tarefaDAO.pesquisaNotas(pesqui);
                    notaAdapter = new NotaAdapter(listaTarefa);

                    break;
                default:

                    listaTarefa = tarefaDAO.listar(n);
                    notaAdapter = new NotaAdapter(listaTarefa);
                    tv_nome_Tab.setText(n);

                    break;
            }

            //configurando recyclerview
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Grid_Largura.calculateNoOfColumns(getApplicationContext(),180), StaggeredGridLayoutManager.VERTICAL);
            recycler_Tarefas.setLayoutManager(layoutManager);
            recycler_Tarefas.setHasFixedSize(true);
            notaAdapter.setHasStableIds(true);
            Collections.reverse(listaTarefa);
            recycler_Tarefas.setAdapter(notaAdapter);
            //notaAdapter.notifyDataSetChanged();
            recycler_Tarefas.setItemViewCacheSize(30);
            recycler_Tarefas.setDrawingCacheEnabled(true);
            recycler_Tarefas.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

            if (listaTarefa.isEmpty()){
                iv_iconVazio.setVisibility(View.VISIBLE);
                Glide.with(iv_iconVazio.getContext()).load(R.drawable.ic_text_branco_gelo).into(iv_iconVazio);
            }else {
                iv_iconVazio.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void carregarCategoria(){

        try {

            //listar tarefas
            final TarefaDAO_Tabela tarefaDAO_tabela = new TarefaDAO_Tabela(getApplicationContext());
            listaTarefas_tabelas = tarefaDAO_tabela.listar();

            //Configurando um adapter
            notaCategoriaAdapter = new NotaCategoriaAdapter(listaTarefas_tabelas);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Grid_Largura.calculateNoOfColumns(getApplicationContext(),180), StaggeredGridLayoutManager.VERTICAL);
            recycler_Categoria.setLayoutManager(layoutManager);
            //layoutManager.setReverseLayout(true);
            recycler_Categoria.setHasFixedSize(false);
            recycler_Categoria.setAdapter(notaCategoriaAdapter);

            notaCategoriaAdapter.cliclado(UltimapositCat);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void escolherAcao(){


        findViewById(R.id.dialog_addNota).setOnClickListener(v -> {

            Intent intent = new Intent(getApplicationContext(), AddNotaActivity.class);
            intent.putExtra("nomeTab",nomeTabela);
            startActivity(intent);

        });

        findViewById(R.id.dialog_addAudio).setOnClickListener(v -> {

            Toast.makeText(this, "Nada aqui", Toast.LENGTH_SHORT).show();

        });

        findViewById(R.id.dialog_addImagem).setOnClickListener(v -> {

            Toast.makeText(this, "Nada aqui", Toast.LENGTH_SHORT).show();

        });

        findViewById(R.id.dialog_scanner).setOnClickListener(v -> {

            Toast.makeText(this, "Nada aqui", Toast.LENGTH_SHORT).show();

        });

    }

    private void criarMinhaNota(){//cria a categoria Minha Nota ao iniciar o app pela primeira vez

        try {

            //Executar acao para salvar item no DB
            TarefaDAO_Tabela tarefaDAO_tabela = new TarefaDAO_Tabela(getApplicationContext());

            List<Tarefas_Tabela> listaTarefas_nome = tarefaDAO_tabela.listarNome("Note");

            if (listaTarefas_nome.isEmpty()){

                Tarefas_Tabela tarefas_tabela = new Tarefas_Tabela();
                tarefas_tabela.setNomeTab("Note");

                if ( tarefaDAO_tabela.salvar(tarefas_tabela)){
                    carregarCategoria();

                    Toast.makeText(getApplicationContext(),R.string.bemVindo,Toast.LENGTH_SHORT).show();
                    /*Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.bemVindo, Snackbar.LENGTH_SHORT);
                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                    snack.show();*/

                    primeiraAcao();

                }

            }

            //passa a posiçao do ultimo item da lista categoria
            Tarefas_Tabela tabela = listaTarefas_tabelas.get(0);
            nomeTabela = tabela.getNomeTab();

        }catch (Exception e){
            e.printStackTrace();

        }


    }

    private void primeiraAcao(){

        try {

            new Handler(Looper.getMainLooper()).postDelayed(() -> { {

                backdropContainer.open();

                new Handler(Looper.getMainLooper()).postDelayed(() -> backdropContainer.close(), 1000);

            }

            }, 1700);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        carregarTarefas(nomeTabela, null);
        super.onStart();
    }

    @Override
    public void onResume() {
        carregarCategoria();
        //preferenciasUsuario();
        bt_exibirTodos.setBackgroundResource(R.drawable.fundo_redondo_preto);
        bt_Favorito.setBackgroundResource(R.drawable.fundo_redondo_preto);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Cria os itens de menu e os ativa no menu
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.bottom_bar_menu, menu);

        getMenuInflater().inflate(R.menu.bottom_bar_menu, menu);
        this.menu = menu;
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

            if (id == R.id.bar_config){

                Toast.makeText(this, "Nada aqui", Toast.LENGTH_SHORT).show();
                return true;

            }else if(id == R.id.bar_lixo){

                Intent in = new Intent(CategoriaActivity.this, NotasExcluidosActivity.class);
                startActivity(in);
                return true;

            }else if (id == R.id.bar_categ){

                addCategoria();
                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    public void addCategoria(){

        try {

            final View cusntom = getLayoutInflater().inflate(R.layout.edit_text_tabela_dialog, null);

            AlertDialog.Builder dialogInterno = new AlertDialog.Builder(CategoriaActivity.this, R.style.DialogStyle);
            //Configurando titulo e mensagem
            dialogInterno.setView(cusntom);
            //dialogInterno.setTitle("Nova Categoria");
            dialogInterno.setMessage(R.string.novaCategoria);

            dialogInterno.setPositiveButton("Ok", (dialog, which) -> {

                EditText editText = cusntom.findViewById(R.id.ed_Tabela_Dialog);

                String nomeT = editText.getText().toString();

                TarefaDAO_Tabela tarefaDAOTabela = new TarefaDAO_Tabela(getApplicationContext());

                if ( !nomeT.isEmpty()){

                    Tarefas_Tabela tarefas_tabela = new Tarefas_Tabela();
                    tarefas_tabela.setNomeTab(nomeT);

                    listaTarefas_nome = tarefaDAOTabela.listarNome(nomeT);

                    if (listaTarefas_nome.isEmpty() && !nomeT.equals("Note") && !nomeT.equals("note") && !nomeT.equals("Minha Nota") && !nomeT.equals("minha nota") && !nomeT.equals("minhanota")){//Verifica se a categoria existe

                        if ( tarefaDAOTabela.salvar(tarefas_tabela)){
                            carregarCategoria();

                        }else {

                            //Toast.makeText(getApplicationContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();
                            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                            snack.show();
                        }

                    }else {

                        //Toast.makeText(getApplicationContext(),R.string.categoriaJaExiste,Toast.LENGTH_SHORT).show();
                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.categoriaJaExiste, Snackbar.LENGTH_SHORT);
                        snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                        snack.show();

                    }

                }else{

                    //Toast.makeText(getApplicationContext(),R.string.nadaAqui,Toast.LENGTH_SHORT).show();
                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.nadaAqui, Snackbar.LENGTH_SHORT);
                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar));
                    snack.show();

                }

            });

            dialogInterno.setNegativeButton(R.string.cancelar, null);

            //Exibir a dialog
            dialog = dialogInterno.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void exibirAvaliacao(){

        try {

            PreferenciaDAO_Usuario dao_usuario = new PreferenciaDAO_Usuario(getApplicationContext());
            List<Preferencia_Usuario> listaTarefas_tabelas = dao_usuario.listarNome();

            Preferencia_Usuario usuario = listaTarefas_tabelas.get(0);

            String Cor = usuario.getCorPrim();
            String sensor = usuario.getAutoPlay();
            String premium = usuario.getPremium();
            int uso = Integer.parseInt(usuario.getExibirQuesti()) + 1;

            if (usuario.getExibirQuesti().equals("6")){// deleta o perfil de preferencias do usuario e cria outro com as informacoes antigas e inclui ExibirQuesti


                AlertDialog.Builder dialogInterno = new AlertDialog.Builder(CategoriaActivity.this, R.style.DialogStyle);
                //Configurando titulo e mensagem
                //dialogInterno.setView(cusntom);
                dialogInterno.setCancelable(false);
                dialogInterno.setTitle(R.string.tituloAvaliacao);
                dialogInterno.setMessage(R.string.mensagemAvaliacao);

                dialogInterno.setPositiveButton(R.string.avaliarAgora, (dialog, which) -> {

                    if (dao_usuario.deletar(usuario)){
                        usuario.setCorPrim(Cor);
                        usuario.setAutoPlay(sensor);
                        usuario.setPremium(premium);
                        usuario.setExibirQuesti("10");

                        if (!dao_usuario.salvar(usuario)) {
                            Toast.makeText(getApplicationContext(), "ERRO", Toast.LENGTH_SHORT).show();

                        }
                    }

                    String url = "https://play.google.com/store/apps/details?id=br.oliver.mark4";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                });

                dialogInterno.setNegativeButton(R.string.depoisAvaliar, (dialog, which) -> {

                    if (dao_usuario.deletar(usuario)){
                        usuario.setCorPrim(Cor);
                        usuario.setAutoPlay(sensor);
                        usuario.setPremium(premium);
                        usuario.setExibirQuesti("2");

                        if (!dao_usuario.salvar(usuario)) {
                            Toast.makeText(getApplicationContext(), "ERRO", Toast.LENGTH_SHORT).show();

                        }
                    }

                });

                //Exibir a dialog
                dialog = dialogInterno.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }else if (!usuario.getExibirQuesti().equals("10")){

                if (dao_usuario.deletar(usuario)) {// deleta o perfil de preferencias do usuario e cria outro com as informacoes antigas e inclui ExibirQuesti

                    usuario.setCorPrim(Cor);
                    usuario.setAutoPlay(sensor);
                    usuario.setPremium(premium);
                    usuario.setExibirQuesti(String.valueOf(uso));

                    if (!dao_usuario.salvar(usuario)) {
                        Toast.makeText(getApplicationContext(), "ERRO", Toast.LENGTH_SHORT).show();

                    }

                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void criarPreferencia(){

        try {

            PreferenciaDAO_Usuario dao_usuario = new PreferenciaDAO_Usuario(getApplicationContext());
            List<Preferencia_Usuario> listaTarefas_tabelas = dao_usuario.listarNome();

            if (listaTarefas_tabelas.isEmpty()) {

                Preferencia_Usuario usuario = new Preferencia_Usuario();

                usuario.setCorPrim("#4D504D");
                usuario.setAutoPlay("0");
                usuario.setPremium("0");
                usuario.setExibirQuesti("0");

                if (!dao_usuario.salvar(usuario)) {
                    Toast.makeText(getApplicationContext(), "ERRO", Toast.LENGTH_SHORT).show();

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        try {

            if (backIcon){
                super.onBackPressed();

            }else {

                backdropContainer.open();

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        //super.onBackPressed();  // optional depending on your needs
    }

}