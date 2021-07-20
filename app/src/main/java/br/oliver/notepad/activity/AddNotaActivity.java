package br.oliver.notepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.oliver.notepad.R;
import br.oliver.notepad.model.Preferencia_Usuario;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.util.PreferenciaDAO_Usuario;
import br.oliver.notepad.util.TarefaDAO;
import jp.wasabeef.richeditor.RichEditor;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;

public class AddNotaActivity extends AppCompatActivity {

    private EditText nomeTarefa;
    private RichEditor nomeDescricao;
    private String Cor, nomeTab;
    private LinearLayout cl_cor, editorTexto;
    private HorizontalScrollView Hs_editor;
    private boolean tarefaNull = false, scrollerAtEnd;
    private AlertDialog dialogPrin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nota);

        //Este metodo chama a toolbar e coloca um titulo
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //botao de voltar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));//altera a cor dos botoes virtuais

        nomeTarefa = findViewById(R.id.nomeTarefa);
        nomeDescricao = findViewById(R.id.nomeDescricao);
        cl_cor = findViewById(R.id.cl_cor);
        editorTexto = findViewById(R.id.editorTexto);
        Hs_editor = findViewById(R.id.Hs_editor);

        editorTexto();
        nomeDescricao.setPlaceholder(getResources().getString(R.string.descricao));

        nomeDescricao.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //recuperando dados da tarefa
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){//verifica se os valores recebidos sao null
            nomeTab = (String) bundle.getSerializable("nomeTab");
        }

        if(nomeTab == null){//verifica se e null
            nomeTab = "Note";
            tarefaNull = true;
        }


        //recuperando texte do scanner
        Bundle textScanner = getIntent().getExtras();
        if (textScanner != null){//verifica se os valores recebidos sao null
            String text = (String) textScanner.getSerializable("textScanner");
            nomeDescricao.setHtml(text);
        }

        PreferenciaDAO_Usuario dao_usuario = new PreferenciaDAO_Usuario(getApplicationContext());
        List<Preferencia_Usuario> listaTarefas_tabelas = dao_usuario.listarNome();

        if(listaTarefas_tabelas.isEmpty()){

            Cor = "#4D504D";

        }else{
            Preferencia_Usuario usuario = listaTarefas_tabelas.get(0);
            Cor = usuario.getCorPrim();

        }

        notaExterna();
        esconderEditor();

        if (savedInstanceState != null) {//recupera o valor quando gira a tela
            String value = savedInstanceState.getString("text");

            nomeDescricao.setHtml(value);
        }
    }

    protected void onSaveInstanceState(@NonNull Bundle extra) {//salva o texto quando raotaciona a tela
        super.onSaveInstanceState(extra);
        extra.putString("text", nomeDescricao.getHtml());
    }

    private void esconderEditor(){

        nomeTarefa.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                //((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(nomeTarefa.getWindowToken(), 0);

                editorTexto.setVisibility(View.VISIBLE);
            }else {

                editorTexto.setVisibility(View.GONE);
            }
        });

        nomeDescricao.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                //((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(nomeDescricao.getWindowToken(), 0);

                editorTexto.setVisibility(View.GONE);
            }else {

                editorTexto.setVisibility(View.VISIBLE);
            }
        });

    }

    private void notaExterna(){// recebe textos de outros aplicativos

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent

            }
        }

    }

    void handleSendText(Intent intent) {// recebe textos de outros aplicativos
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            nomeDescricao.setHtml(sharedText);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        //Cria os itens de menu e os ativa no menu
        //MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_salvar_tarefa,menu);

        getMenuInflater().inflate(R.menu.menu_salvar_tarefa, menu);
        menu.getItem(0).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

            if (id == R.id.menu_save) {

                String nomeT = nomeTarefa.getText().toString();
                String nomeDes = nomeDescricao.getHtml();

                if (!nomeT.isEmpty() || (nomeDes != null && !nomeDes.equals(""))){


                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddNotaActivity.this);
                    dialog.setMessage(R.string.descartaraNota);

                    //exclui a pasta
                    dialog.setPositiveButton(R.string.sim, (dialog12, which) -> finish());
                    dialog.setNegativeButton(R.string.nao, (dialog1, which) -> {

                        //getOnBackPressedDispatcher();

                    });

                    //Exibir a dialog
                    dialog.create();
                    dialog.show();

                }else{

                    finish();
                }

                return true;

            }else if (id == R.id.menu_cor){

                new ColorPickerDialog()
                        .withTitle(getResources().getString(R.string.escolhaSuaCor))
                        .withAlphaEnabled(false)
                        .withCornerRadius(16)
                        .withColor(Color.parseColor(Cor)) // the default / initial color

                        .withListener((dialog, color) -> {

                            //Toast.makeText(AddNotaActivity.this, String.format("#%08X", color), Toast.LENGTH_SHORT).show();

                            Cor = String.format("#%08X", color);
                            // fab_Cor.setBackgroundTintList(ColorStateList.valueOf((Color.parseColor(Cor))));,
                            cl_cor.setBackgroundColor(Color.parseColor(Cor));
                        })
                        .show(getSupportFragmentManager(), "colorPicker");

                return true;
            }else if (id == R.id.menu_compartilhar){

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, fromHtml(nomeDescricao.getHtml()).toString());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

                return true;
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

            try {

                String nomeDes = nomeDescricao.getHtml();
                String nomeT = nomeTarefa.getText().toString();

                if (!nomeDes.isEmpty()){


                Date dataHoraAtual = new Date();
                Date HoraAtual = new Date();
                @SuppressLint("SimpleDateFormat") String dataCriacao = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
                @SuppressLint("SimpleDateFormat") String horCriacao = new SimpleDateFormat("hh:mm").format(HoraAtual);


                //Executar acao para salvar item no BD
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                Tarefa tarefa = new Tarefa();


                    tarefa.setDataCriacao(dataCriacao);
                    tarefa.setComSenha("0");
                    tarefa.setHoraCriacao(horCriacao);
                    tarefa.setNomeTabela(nomeTab);
                    tarefa.setComAudio("0");
                    tarefa.setComImagem("0");
                    tarefa.setCaminhoImagem("");
                    tarefa.setCaminhoAudio("");
                    tarefa.setFavorito("0");
                    tarefa.setNomeDescricao(nomeDes);

                    if (nomeT.isEmpty()) {

                        tarefa.setNomeTarefa(getResources().getString(R.string.texto));

                    } else {

                        tarefa.setNomeTarefa(nomeT);
                    }

                    //verifica se escolheu uma cor
                    if (Cor == null) {
                        tarefa.setCor(Cor = "#4D504D");
                        //Toast.makeText(AddNotaActivity.this, Cor, Toast.LENGTH_SHORT).show();
                    } else {
                        tarefa.setCor(Cor);
                    }

                    if (tarefaNull){

                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.salvaEm) + " " + nomeTab, Toast.LENGTH_SHORT).show();
                    }

                    if (tarefaDAO.salvar(tarefa)) {
                        finish();
                        //Toast.makeText(getApplicationContext(),"Tarefa salva",Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(getApplicationContext(), R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();

                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                        snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_barra_edit));
                        snack.show();
                    }

                }else{

                    finish();
                }

            }catch (Exception e){
                e.printStackTrace();

                Toast.makeText(AddNotaActivity.this, R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();

                finish();

            }

        //super.onBackPressed();  // optional depending on your needs
    }

    private void editorTexto(){//funcoes do editor de texto


        findViewById(R.id.action_undo).setOnClickListener(v -> nomeDescricao.undo());

        findViewById(R.id.action_redo).setOnClickListener(v -> nomeDescricao.redo());

        findViewById(R.id.action_size).setOnClickListener(v -> {


            AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddNotaActivity.this, R.style.DialogStyle);
            builderSingle.setIcon(R.drawable.ic_text_size_preto);
            builderSingle.setTitle(R.string.tamanhoDaLetra);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddNotaActivity.this, android.R.layout.select_dialog_item);
            arrayAdapter.add("Grande");
            arrayAdapter.add("Medio");
            arrayAdapter.add("Normal");
            arrayAdapter.add("Pequeno");

            builderSingle.setNegativeButton(R.string.cancelar, (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);

                switch (strName) {
                    case "Grande":
                        nomeDescricao.setHeading(2);
                        break;
                    case "Medio":
                        nomeDescricao.setHeading(3);
                        break;
                    case "Normal":
                        nomeDescricao.setHeading(4);
                        break;

                    case "Pequeno":
                        nomeDescricao.setHeading(5);
                        break;
                }

            });

            dialogPrin = builderSingle.create();
            dialogPrin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogPrin.show();

        });

        findViewById(R.id.action_bold).setOnClickListener(v -> nomeDescricao.setBold());

        findViewById(R.id.action_italic).setOnClickListener(v -> nomeDescricao.setItalic());
        findViewById(R.id.action_strikethrough).setOnClickListener(v -> nomeDescricao.setStrikeThrough());

        findViewById(R.id.action_underline).setOnClickListener(v -> nomeDescricao.setUnderline());

        findViewById(R.id.action_marcar_texto).setOnClickListener(new View.OnClickListener() {

            private boolean isChanged;
            @Override
            public void onClick(View v) {
                nomeDescricao.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;

            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(v -> nomeDescricao.setAlignLeft());

        findViewById(R.id.action_align_center).setOnClickListener(v -> nomeDescricao.setAlignCenter());

        findViewById(R.id.action_align_right).setOnClickListener(v -> nomeDescricao.setAlignRight());
        findViewById(R.id.action_insert_bullets).setOnClickListener(v -> nomeDescricao.setBullets());

        findViewById(R.id.action_insert_numbers).setOnClickListener(v -> nomeDescricao.setNumbers());

        initToolbarArrow();
    }

    private void initToolbarArrow() {
        final ImageView imageView = this.findViewById(R.id.img_arrow);

            Hs_editor.getViewTreeObserver().addOnScrollChangedListener(() -> {
                int scrollX = Hs_editor.getScrollX();
                int scrollWidth = Hs_editor.getWidth();
                int fullWidth = Hs_editor.getChildAt(0).getWidth();

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
                Hs_editor.smoothScrollBy(-Integer.MAX_VALUE, 0);
                scrollerAtEnd = false;
            } else {
                int hsWidth = Hs_editor.getChildAt(0).getWidth();
                Hs_editor.smoothScrollBy(hsWidth, 0);
                scrollerAtEnd = true;
            }
        });
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
}