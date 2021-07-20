package br.oliver.notepad.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.concurrent.Executor;

import br.oliver.notepad.R;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.util.TarefaDAO;

public class SenhaActivity extends AppCompatActivity {

    private Tarefa tarefaAtual;
    private Button fabSenha, fabSenha1;
    private String senha;
    private EditText eTNP;
    private Toolbar toolbar;
    private boolean pass, estaComSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_senha);

        toolbar = findViewById(R.id.toolbarSenha);
        toolbar.setTitle(R.string.novaSenha);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));//altera a cor dos botoes virtuais

        eTNP = findViewById(R.id.eTNP);
        fabSenha = findViewById(R.id.btSenha2);
        fabSenha1 = findViewById(R.id.btSenha);

        this.eTNP.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN) {

                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {

                    if (estaComSenha){

                        funcaoSenha1(v);

                    }else {

                        if (pass){

                            funcaoSenha1(v);

                        }else {

                            funcaoSenha(v);

                        }

                    }

                    return true;
                }

            }
            return false;
        });

        //Recuperar tarefa ,caso seja edicao e verifica da onde esta vindo os dados
        if (getIntent().getSerializableExtra("senha") == null){

            tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");
            //btSenha.setText("Ok");
            biometria();
            fabSenha1.setVisibility(View.VISIBLE);
            fabSenha.setVisibility(View.INVISIBLE);

            toolbar.setTitle(R.string.digiteSenha);

            estaComSenha = true;

        }else{

            fabSenha1.setVisibility(View.INVISIBLE);
            tarefaAtual = (Tarefa) getIntent().getSerializableExtra("senha");

            estaComSenha = false;
        }

        fabSenha.setOnClickListener(this::funcaoSenha);


        fabSenha1.setOnClickListener(this::funcaoSenha1);

    }

    private void funcaoSenha(View view){

        senha = eTNP.getText().toString();

        pass = true;

        if (!senha.isEmpty()) {

            senha = eTNP.getText().toString();
            eTNP.setText("");
            fabSenha1.setVisibility(View.VISIBLE);
            fabSenha.setVisibility(View.INVISIBLE);
            fabSenha1.setText(R.string.trancar);

            toolbar.setTitle(R.string.redigiteSenha);

        }else{

            //Toast.makeText(getApplicationContext(), R.string.campoVazio, Toast.LENGTH_SHORT).show();

            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.campoVazio, Snackbar.LENGTH_SHORT);
            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_bottom));
            snack.show();


            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


        }

    }

    private void funcaoSenha1(View view){

        String nomeT = eTNP.getText().toString();

        if(tarefaAtual.getComSenha().equals("1")){//verifica se as senha e valida

            if (nomeT.equals(tarefaAtual.getSenhaNota())){


                if (tarefaAtual.getComAudio().equals("1")){

                    Toast.makeText(this, "Nada aqui", Toast.LENGTH_SHORT).show();

                }else if(tarefaAtual.getComAudio().equals("0") || tarefaAtual.getComImagem().equals("0")){

                    Intent intent = new Intent(SenhaActivity.this, EditarNotaActivity.class);
                    intent.putExtra("tarefaSelecionada",tarefaAtual);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                }else if (tarefaAtual.getComImagem().equals("1")) {

                    Toast.makeText(this, "Nada aqui", Toast.LENGTH_SHORT).show();
                }

                finish();

            }else{
                eTNP.setText("");

                //Toast.makeText(getApplicationContext(), R.string.senhaErrada, Toast.LENGTH_SHORT).show();

                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.senhaErrada, Snackbar.LENGTH_SHORT);
                snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_bottom));
                snack.show();

                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }

        }else{

            pass = false;

            //Configurar tarefa na caixa de texto
            if(tarefaAtual != null) {// insere uma nova senha

                //Executar acao para salvar item no DB
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                if (!nomeT.isEmpty()) {

                    if(senha.equals(nomeT)){

                        Tarefa tarefa = new Tarefa();

                        tarefa.setId(tarefaAtual.getId());
                        tarefa.setComSenha("1");
                        tarefa.setSenhaNota(nomeT);

                        if (tarefaDAO.atualizarSenha(tarefa)) {
                            //Toast.makeText(EditarTarefaActivity.this, "Atualizado!", Toast.LENGTH_SHORT).show();

                            finish();

                        } else {

                            //Toast.makeText(SenhaActivity.this, R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();

                            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                            snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_bottom));
                            snack.show();

                            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        }

                    }else{

                        //Toast.makeText(getApplicationContext(), R.string.senhasDiferentes, Toast.LENGTH_SHORT).show();

                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.senhasDiferentes, Snackbar.LENGTH_SHORT);
                        snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_bottom));
                        snack.show();

                        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        senha = "";
                        eTNP.setText("");
                        fabSenha.setVisibility(View.VISIBLE);
                        fabSenha1.setVisibility(View.INVISIBLE);
                        toolbar.setTitle(R.string.novaSenha);
                    }

                }else{

                    //Toast.makeText(getApplicationContext(), R.string.campoVazio, Toast.LENGTH_SHORT).show();

                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.campoVazio, Snackbar.LENGTH_SHORT);
                    snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_bottom));
                    snack.show();

                    InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                }

            }else{

                //Toast.makeText(SenhaActivity.this, R.string.algoDeuErrado, Toast.LENGTH_SHORT).show();

                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), R.string.algoDeuErrado, Snackbar.LENGTH_SHORT);
                snack.getView().setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.container_snackbar_bottom));
                snack.show();

                InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }

    }

    public void biometria(){

        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(SenhaActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                //Toast.makeText(getApplicationContext(), "Seu dispositivo nao tem biometria :(", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //Toast.makeText(getApplicationContext(), "Authentication succeeded!", Toast.LENGTH_SHORT).show();


                if (tarefaAtual.getComAudio().equals("1")) {

                    Toast.makeText(getApplicationContext(), "Nada aqui", Toast.LENGTH_SHORT).show();

                } else if (tarefaAtual.getComAudio().equals("0") || tarefaAtual.getComImagem().equals("0")) {

                    Intent intent = new Intent(getApplicationContext(), EditarNotaActivity.class);
                    intent.putExtra("tarefaSelecionada", tarefaAtual);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);

                } else if (tarefaAtual.getComImagem().equals("1")) {

                    Toast.makeText(getApplicationContext(), "Nada aqui", Toast.LENGTH_SHORT).show();
                }

                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getResources().getString(R.string.autenticaçãoBiometria))
                .setSubtitle(getResources().getString(R.string.paraAcessarDigital))
                .setNegativeButtonText(getResources().getString(R.string.senhaNumérica))
                .build();

        biometricPrompt.authenticate(promptInfo);


    }

    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();  // optional depending on your needs
    }

    /*public void excluirAudio() {

        //path = "caminho da pasta/arquivo, etc";

        boolean p = new File(tarefaAtual.getCaminhoAudio()).delete();

        if(p){

            //Toast.makeText(SenhaActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(SenhaActivity.this, "Erro", Toast.LENGTH_SHORT).show();

        }

    }

    public void excluirImg() {

        //path = "caminho da pasta/arquivo, etc";

        boolean p = new File(tarefaAtual.getCaminhoImagem()).delete();

        if(p){

            //Toast.makeText(EditarImagemActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();

        }else{

            Toast.makeText(SenhaActivity.this, "Erro", Toast.LENGTH_SHORT).show();

        }

    }*/

    
}