package br.oliver.notepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.oliver.notepad.R;
import br.oliver.notepad.adapter.AlterarCategoriaNotaAdapter;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.model.Tarefa_Lixeira;
import br.oliver.notepad.model.Tarefas_Tabela;
import br.oliver.notepad.util.Permissao;
import br.oliver.notepad.util.RecyclerItemClickListener;
import br.oliver.notepad.util.TarefaDAO;
import br.oliver.notepad.util.TarefaDAO_Lixeira;
import br.oliver.notepad.util.TarefaDAO_Tabela;
import jp.wasabeef.richeditor.RichEditor;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;


public class EditarNotaActivity extends AppCompatActivity {

    private final String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private EditText editTarefa;
    private RichEditor editDescricao;
    private Tarefa tarefaAtual;
    private String Cor;
    private LinearLayout cl_cores, editorTexto_edit;
    private HorizontalScrollView Hs_editor_edit;
    private boolean scrollerAtEnd, printBtnPressed = false;
    private PrintJob printJob;

    private AlertDialog dialogPrin;
    private Menu menu;

    //invoca os dados da tabela
    private AlterarCategoriaNotaAdapter tarefaTabelaAdapter;
    private List<Tarefas_Tabela> listaTarefas_tabelas = new ArrayList<>();
    private Tarefas_Tabela tabelaSelecionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_nota);


        carregar();
        esconderEditor();
        editorTexto();

        //Validar permissoes
        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        //listar tarefas da tabela
        TarefaDAO_Tabela tarefaDAO_tabela = new TarefaDAO_Tabela(getApplicationContext());
        listaTarefas_tabelas = tarefaDAO_tabela.listar();

        //Configurando um adapter da tabela
        tarefaTabelaAdapter = new AlterarCategoriaNotaAdapter(listaTarefas_tabelas);


        if (savedInstanceState != null) {//recupera o valor quando gira a tela
            String value = savedInstanceState.getString("text");

            editDescricao.setHtml(value);
        }
    }

    private void carregar(){

        //Este metodo chama a toolbar e coloca um titulo
        Toolbar toolbar = findViewById(R.id.toobal_editar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));//altera a cor dos botoes virtuais

        //botao de voltar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editTarefa = findViewById(R.id.editTarefa);
        editDescricao = findViewById(R.id.editDescricao);
        cl_cores = findViewById(R.id.cl_cores);
        editorTexto_edit = findViewById(R.id.editorTexto_edit);
        Hs_editor_edit = findViewById(R.id.Hs_editor_edit);


        editDescricao.setPlaceholder(getResources().getString(R.string.descricao));

        editDescricao.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //Recuperar tarefa ,caso seja edicao
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //Configurar tarefa na caixa de texto
        if(tarefaAtual != null) {

            editTarefa.setText(tarefaAtual.getNomeTarefa());
            editDescricao.setHtml(tarefaAtual.getNomeDescricao());

            //recupera as cores
            if(tarefaAtual.getCor() == null){

                cl_cores.setBackgroundTintList(ColorStateList.valueOf((ContextCompat.getColor(getBaseContext(),R.color.colorAccent))));

            }else {
                Cor = tarefaAtual.getCor();
                cl_cores.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor(tarefaAtual.getCor()))));
            }


        }

    }

    protected void onSaveInstanceState(@NonNull Bundle extra) {//salva o texto quando raotaciona a tela
        super.onSaveInstanceState(extra);
        extra.putString("text", editDescricao.getHtml());
    }

    private void esconderEditor(){

        editTarefa.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                //((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(nomeTarefa.getWindowToken(), 0);

                editorTexto_edit.setVisibility(View.VISIBLE);
            }else {
                editorTexto_edit.setVisibility(View.GONE);
            }
        });

        editDescricao.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                //((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(nomeDescricao.getWindowToken(), 0);

                editorTexto_edit.setVisibility(View.GONE);
            }else {
                editorTexto_edit.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //Cria os itens de menu e os ativa no menu
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_editar_nota,menu);

        getMenuInflater().inflate(R.menu.menu_editar_tarefa, menu);

        if (tarefaAtual.getComSenha().equals("1")){

            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_lock_preto));

        }

        if (tarefaAtual.getFavorito().equals("1")){
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_marcado));

        }

        menu.getItem(4).setVisible(true);
        menu.getItem(5).setVisible(true);

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();


            if (id == R.id.bar_excluir) {

                try {
                String nomeT = editTarefa.getText().toString();
                String nomeDes = editDescricao.getHtml();

                Date dataHoraAtual = new Date();
                Date HoraAtual = new Date();
                @SuppressLint("SimpleDateFormat") String dataCriacao = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
                @SuppressLint("SimpleDateFormat") String horCriacao = new SimpleDateFormat("HH:mm").format(HoraAtual);

                //Executar acao para salvar item no DB
                TarefaDAO_Lixeira tarefaDAO_lixeira = new TarefaDAO_Lixeira(getApplicationContext());

                if ( !nomeT.isEmpty()) {
                    Tarefa_Lixeira tarefa_concluida = new Tarefa_Lixeira();
                    tarefa_concluida.setId_lixeira(tarefaAtual.getId());
                    tarefa_concluida.setNomeTabela_lixeira(tarefaAtual.getNomeTabela());
                    tarefa_concluida.setNomeTarefa_lixeira(nomeT);
                    tarefa_concluida.setNomeDescricao_lixeira(nomeDes);
                    tarefa_concluida.setCor_lixeira(Cor);
                    tarefa_concluida.setComSenha_lixeira("0");
                    tarefa_concluida.setSenhaNota_lixeira("");
                    tarefa_concluida.setDataCriacao_lixeira(dataCriacao);
                    tarefa_concluida.setHoraCriacao_lixeira(horCriacao);
                    tarefa_concluida.setComAudio_lixeira(tarefaAtual.getComAudio());
                    tarefa_concluida.setCaminhoAudio_lixeira(tarefaAtual.getCaminhoAudio());
                    tarefa_concluida.setComImagem_lixeira(tarefaAtual.getComImagem());
                    tarefa_concluida.setCaminhoImagem_lixeira(tarefaAtual.getCaminhoImagem());


                    if (!tarefaDAO_lixeira.salvar(tarefa_concluida)) {
                        //Toast.makeText(EditarTarefaActivity.this, "Atualizado!", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(EditarNotaActivity.this, R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();

                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                        snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                        snack.show();

                    }

                }else {

                    //Toast.makeText(EditarNotaActivity.this, R.string.tituloVazio, Toast.LENGTH_SHORT).show();

                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.tituloVazio, Snackbar.LENGTH_SHORT);
                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                    snack.show();

                }

                    concluido();

                    Toast.makeText(getApplicationContext(),R.string.movidoLixeira,Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                    //Toast.makeText(EditarImagemActivity.this, "Algo deu errado ", Toast.LENGTH_SHORT).show();
                }

                return true;

            }else if (id == R.id.bar_cor){

                new ColorPickerDialog()
                        .withTitle(getResources().getString(R.string.escolhaSuaCor))
                        .withColor(Color.parseColor(Cor))
                        .withAlphaEnabled(false)
                        .withCornerRadius(16)// the default / initial color
                        .withListener((dialog, color) -> {

                            //Toast.makeText(EditarTarefaActivity.this, String.format("#%08X", color), Toast.LENGTH_SHORT).show();

                            Cor = String.format("#%08X", color);
                            cl_cores.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor(Cor))));
                        })
                        .show(getSupportFragmentManager(), "colorPicker");


                return true;

            }else if (id == R.id.bar_pasta){

                AlertDialog.Builder dialo = new AlertDialog.Builder(EditarNotaActivity.this, R.style.DialogStyle);
                dialo.setTitle(getResources().getString(R.string.moverPara));
                LayoutInflater inflater = getLayoutInflater();
                View convertView = inflater.inflate(R.layout.recyclerview_categoria_dialog, null);

                RecyclerView list = convertView.findViewById(R.id.recyclerTabela);
                list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                list.setAdapter(tarefaTabelaAdapter);
                dialo.setView(convertView); // setView

                tarefaTabelaAdapter.marcar(tarefaAtual.getNomeTabela());

                list.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), list, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //Recuperando tarefa para edicao
                        tabelaSelecionada = listaTarefas_tabelas.get(position);

                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());


                        Tarefa tarefa = new Tarefa();
                        tarefa.setId(tarefaAtual.getId());
                        tarefa.setNomeTabela(tabelaSelecionada.getNomeTab());

                        if (tarefaDAO.atualizarNomeTab(tarefa)) {

                            if (!tarefaAtual.getNomeTabela().equals(tabelaSelecionada.getNomeTab())){

                                Toast.makeText(EditarNotaActivity.this, getResources().getString(R.string.movidoPara) + " "  + tabelaSelecionada.getNomeTab(), Toast.LENGTH_SHORT).show();

                                dialogPrin.dismiss();

                                finish();
                            }else {

                                dialogPrin.dismiss();
                                //Toast.makeText(EditarNotaActivity.this, R.string.categoriaAtual, Toast.LENGTH_SHORT).show();

                                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.categoriaAtual, Snackbar.LENGTH_SHORT);
                                snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                                snack.show();

                            }

                        } else {

                            //Toast.makeText(EditarNotaActivity.this, R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();

                            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                            snack.show();
                        }


                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }));

                dialogPrin = dialo.create();

                dialogPrin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogPrin.show();

                return true;

            }else if (id == R.id.bar_bloquear){

                if (tarefaAtual.getComSenha().equals("1")){//remove a senha

                    AlertDialog.Builder dia = new AlertDialog.Builder(EditarNotaActivity.this);
                    //Configurando titulo e mensagem
                    dia.setMessage(R.string.desejaRemoverSenha);

                    //exclui a pasta
                    dia.setPositiveButton(R.string.sim, (dialog, which) -> {

                        tarefaAtual.setComSenha("0");
                        tarefaAtual.setSenhaNota("");

                        //Toast.makeText(EditarNotaActivity.this, R.string.senhaRemovida, Toast.LENGTH_SHORT).show();
                        menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_lock_open));

                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.senhaRemovida, Snackbar.LENGTH_SHORT);
                        snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                        snack.show();

                    });
                    dia.setNegativeButton(R.string.nao, null);

                    //Exibir a dialog
                    dia.create();
                    dia.show();

                }else{//vai para inserir senha

                    Intent intent = new Intent(getApplicationContext(), SenhaActivity.class);
                    intent.putExtra("senha",tarefaAtual);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);

                    finish();

                }

                return true;

            }else if (id == R.id.bar_compartilhar){

                //Document doc = Jsoup.parse(editDescricao.getHtml());
                //String txt = doc.body().text();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, fromHtml(editDescricao.getHtml()).toString());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                return true;

            }else if (id == R.id.bar_favorito){

                if (tarefaAtual.getFavorito().equals("1")){//remove favorito

                    tarefaAtual.setFavorito("0");
                    //Toast.makeText(EditarNotaActivity.this, R.string.desmarcarfavoritos, Toast.LENGTH_SHORT).show();
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorito_preto));

                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.desmarcarfavoritos, Snackbar.LENGTH_SHORT);
                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                    snack.show();

                }else{//marca favorito

                    //Toast.makeText(EditarNotaActivity.this, R.string.marcarfavoritos, Toast.LENGTH_SHORT).show();
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_marcado));

                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.marcarfavoritos, Snackbar.LENGTH_SHORT);
                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                    snack.show();

                    tarefaAtual.setFavorito("1");

                }

                return true;

            }else if (id == R.id.bar_pdf){

                try {
                    printBtnPressed = true;

                    // Creating  PrintManager instance
                    PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

                    // setting the name of job
                    String jobName = "Note_"+System.currentTimeMillis();

                    // Creating  PrintDocumentAdapter instance
                    PrintDocumentAdapter printAdapter = editDescricao.createPrintDocumentAdapter(jobName);

                    // Create a print job with name and adapter instance
                    assert printManager != null;
                    printJob = printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());

                }catch (Exception e){
                    e.printStackTrace();
                }

                return true;

            }else if (id == R.id.bar_txt){

                try {

                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File myFile = new File(path, "Note_"+System.currentTimeMillis()+".docx");
                    FileOutputStream fOut = new FileOutputStream(myFile,true);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    myOutWriter.append(editDescricao.getHtml());
                    myOutWriter.close();
                    fOut.close();

                    Toast.makeText(this,R.string.salvoEmDownload,Toast.LENGTH_LONG).show();

                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    public static Spanned fromHtml(String html){
        if(html == null){
            // return an empty spannable if the html is null
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (printJob != null && printBtnPressed) {
            if (printJob.isCompleted()) {
                // Showing Toast Message
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
            } else if (printJob.isStarted()) {
                // Showing Toast Message
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show();

            } else if (printJob.isBlocked()) {
                // Showing Toast Message
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show();

            } else if (printJob.isCancelled()) {
                // Showing Toast Message
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show();

            } else if (printJob.isFailed()) {
                // Showing Toast Message
                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show();

            } else if (printJob.isQueued()) {
                // Showing Toast Message
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show();

            }
            // set printBtnPressed false
            printBtnPressed = false;
        }
    }

    private void concluido(){

        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        if (tarefaDAO.deletar(tarefaAtual)){
            finish();
            //Toast.makeText(getApplicationContext(),R.string.movidoLixeira,Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(getApplicationContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();

            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
            snack.show();
        }

    }

    @Override
    public void onBackPressed() {//salva as alteracoes

        try {

        String nomeT = editTarefa.getText().toString();
        String nomeDes = editDescricao.getHtml();

        //Executar acao para salvar item no DB
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

        if ( !nomeT.isEmpty() && !nomeDes.isEmpty()) {
            Tarefa tarefa = new Tarefa();
            tarefa.setId(tarefaAtual.getId());
            //tarefa.setNomeTabela(tarefaAtual.getNomeTabela());
            tarefa.setNomeTarefa(nomeT);
            tarefa.setNomeDescricao(nomeDes);
            tarefa.setCor(Cor);
            tarefa.setComSenha(tarefaAtual.getComSenha());
            tarefa.setSenhaNota(tarefaAtual.getSenhaNota());
            tarefa.setDataCriacao(tarefaAtual.getDataCriacao());
            tarefa.setHoraCriacao(tarefaAtual.getHoraCriacao());
            tarefa.setComAudio("0");
            tarefa.setCaminhoAudio("");
            tarefa.setComImagem(tarefaAtual.getComImagem());
            tarefa.setFavorito(tarefaAtual.getFavorito());


            if (tarefaDAO.atualizar(tarefa)) {
                finish();

                //Toast.makeText(EditarNotaActivity.this, "Atualizado", Toast.LENGTH_SHORT).show();

            } else {

                //Toast.makeText(EditarNotaActivity.this, R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();

                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                snack.show();
            }

        }else {

            //Toast.makeText(EditarNotaActivity.this, R.string.tituloVazio, Toast.LENGTH_SHORT).show();

            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.tituloVazio, Snackbar.LENGTH_SHORT);
            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
            snack.show();

        }

        }catch (Exception e){
            e.printStackTrace();

        }

        //super.onBackPressed();  // optional depending on your needs
    }

    private void editorTexto(){//funcoes do editor de texto

        findViewById(R.id.action_undo).setOnClickListener(v -> editDescricao.undo());

        findViewById(R.id.action_redo).setOnClickListener(v -> editDescricao.redo());

        findViewById(R.id.action_size).setOnClickListener(v -> {


            AlertDialog.Builder builderSingle = new AlertDialog.Builder(EditarNotaActivity.this, R.style.DialogStyle);
            builderSingle.setIcon(R.drawable.ic_text_size_preto);
            builderSingle.setTitle(R.string.tamanhoDaLetra);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(EditarNotaActivity.this, android.R.layout.select_dialog_item);
            arrayAdapter.add("Grande");
            arrayAdapter.add("Medio");
            arrayAdapter.add("Normal");
            arrayAdapter.add("Pequeno");

            builderSingle.setNegativeButton(R.string.cancelar, (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);

                switch (strName) {
                    case "Grande":
                        editDescricao.setHeading(2);
                        break;
                    case "Medio":
                        editDescricao.setHeading(3);
                        break;
                    case "Normal":
                        editDescricao.setHeading(4);
                        break;

                    case "Pequeno":
                        editDescricao.setHeading(5);
                        break;
                }

            });

            dialogPrin = builderSingle.create();
            dialogPrin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogPrin.show();

        });

        findViewById(R.id.action_bold).setOnClickListener(v -> editDescricao.setBold());

        findViewById(R.id.action_italic).setOnClickListener(v -> editDescricao.setItalic());
        findViewById(R.id.action_strikethrough).setOnClickListener(v -> editDescricao.setStrikeThrough());

        findViewById(R.id.action_underline).setOnClickListener(v -> editDescricao.setUnderline());

        findViewById(R.id.action_marcar_texto).setOnClickListener(new View.OnClickListener() {

            private boolean isChanged;
            @Override
            public void onClick(View v) {
                editDescricao.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;

            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(v -> editDescricao.setAlignLeft());

        findViewById(R.id.action_align_center).setOnClickListener(v -> editDescricao.setAlignCenter());

        findViewById(R.id.action_align_right).setOnClickListener(v -> editDescricao.setAlignRight());
        findViewById(R.id.action_insert_bullets).setOnClickListener(v -> editDescricao.setBullets());

        findViewById(R.id.action_insert_numbers).setOnClickListener(v -> editDescricao.setNumbers());

        initToolbarArrow();
    }

    private void initToolbarArrow() {
        final ImageView imageView = this.findViewById(R.id.img_arrow_edit);

        Hs_editor_edit.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollX = Hs_editor_edit.getScrollX();
            int scrollWidth = Hs_editor_edit.getWidth();
            int fullWidth = Hs_editor_edit.getChildAt(0).getWidth();

            if (scrollX + scrollWidth < fullWidth) {
                imageView.setImageResource(R.drawable.ic_arrow_right_preto);
                scrollerAtEnd = false;
            } else {
                imageView.setImageResource(R.drawable.ic_arrow_left_preto);
                scrollerAtEnd = true;
            }
        });


        imageView.setOnClickListener(view -> {
            if (scrollerAtEnd) {
                Hs_editor_edit.smoothScrollBy(-Integer.MAX_VALUE, 0);
                scrollerAtEnd = false;
            } else {
                int hsWidth = Hs_editor_edit.getChildAt(0).getWidth();
                Hs_editor_edit.smoothScrollBy(hsWidth, 0);
                scrollerAtEnd = true;
            }
        });
    }

    @Override //Verifica se o usuario negou as permissoes do app
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if (permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permissoesNegadas);
        builder.setMessage(R.string.textoPermissao);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", (dialog, which) -> finish());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}