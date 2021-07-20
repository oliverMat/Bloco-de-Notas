package br.oliver.notepad.adapter;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.List;
import br.oliver.notepad.R;
import br.oliver.notepad.activity.EditarNotaActivity;
import br.oliver.notepad.activity.SenhaActivity;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.util.TarefaDAO;


public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.MyViewHolder> {

    private final List<Tarefa> listaTarefas;
    private Context context;
    private BottomSheetDialog dialog;

    public NotaAdapter(List<Tarefa> lista) {
        this.listaTarefas = lista;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_nota, viewGroup,false);

        context = viewGroup.getContext();

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {


            final Tarefa tarefa = listaTarefas.get(i);

            if(tarefa.getComSenha().equals("1")){//se esta com senha ele bloqueia o conteudo

                myViewHolder.tarefa.setText(tarefa.getNomeTarefa());

                myViewHolder.iVIcon_Img.setVisibility(View.VISIBLE);
                myViewHolder.descricao.setVisibility(View.GONE);
                myViewHolder.tv_TempoAudio.setVisibility(View.GONE);


                ViewGroup.LayoutParams params = myViewHolder.iVIcon_Img.getLayoutParams();
                // Changes the height and width to the specified *pixels*
                params.height = 190;
                params.width = 190;
                myViewHolder.iVIcon_Img.setLayoutParams(params);


                if (tarefa.getComAudio().equals("0") || tarefa.getComImagem().equals("0")) {

                    Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_text).into(myViewHolder.headerIndicator);

                }else if(tarefa.getComImagem().equals("1")){

                    Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_galeria).into(myViewHolder.headerIndicator);

                }else if(tarefa.getComAudio().equals("1")){

                    Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_mic).into(myViewHolder.headerIndicator);

                }else if(tarefa.getFavorito().equals("1")){

                    Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_favorito_preto).into(myViewHolder.headerIndicator);

                }


            }else if(tarefa.getComSenha().equals("0")) {

                if (tarefa.getComAudio().equals("0") || tarefa.getComImagem().equals("0")) {

                    try {

                        myViewHolder.tarefa.setText(tarefa.getNomeTarefa());
                        myViewHolder.descricao.setText(fromHtml(tarefa.getNomeDescricao()));

                        if (tarefa.getFavorito().equals("1")){

                            Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_favorito_preto).into(myViewHolder.headerIndicator);

                        }else {

                            Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_text).into(myViewHolder.headerIndicator);
                        }


                        myViewHolder.iVIcon_Img.setVisibility(View.GONE);
                        myViewHolder.descricao.setVisibility(View.VISIBLE);
                        myViewHolder.tv_TempoAudio.setVisibility(View.GONE);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else if(tarefa.getComImagem().equals("1")){

                    try {

                        myViewHolder.tarefa.setText(tarefa.getNomeTarefa());

                        if (tarefa.getFavorito().equals("1")){

                            Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_favorito_preto).into(myViewHolder.headerIndicator);

                        }else {

                            Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_galeria).into(myViewHolder.headerIndicator);
                        }

                        Glide.with(myViewHolder.itemView.getContext()).load(tarefa.getCaminhoImagem()).into(myViewHolder.iVIcon_Img);


                        ViewGroup.LayoutParams params = myViewHolder.iVIcon_Img.getLayoutParams();
                        // Changes the height and width to the specified *pixels*
                        params.height = 600;
                        //params.width = 200;
                        myViewHolder.iVIcon_Img.setLayoutParams(params);
                        myViewHolder.iVIcon_Img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        myViewHolder.iVIcon_Img.setClipToOutline(true);


                        myViewHolder.iVIcon_Img.setVisibility(View.VISIBLE);
                        myViewHolder.descricao.setVisibility(View.GONE);
                        myViewHolder.tv_TempoAudio.setVisibility(View.GONE);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else if(tarefa.getComAudio().equals("1")){

                    try {

                        myViewHolder.tarefa.setText(tarefa.getNomeTarefa());

                        if (tarefa.getFavorito().equals("1")){
                            Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_favorito_preto).into(myViewHolder.headerIndicator);
                        }else {
                            Glide.with(myViewHolder.headerIndicator.getContext()).load(R.drawable.ic_mic).into(myViewHolder.headerIndicator);
                        }


                        MediaPlayer mediaPlayer = MediaPlayer.create(myViewHolder.itemView.getContext(), Uri.parse(tarefa.getCaminhoAudio()));
                        int duration  = mediaPlayer.getDuration()/1000; // In milliseconds

                        long aux2 ;
                        int min2, sec2;
                        aux2 = duration;

                        min2 = (int) (aux2 / 60);
                        sec2 = (int) (aux2 % 60);

                        String sDura2 = min2 < 10 ? "0"+min2 : min2 + "";

                        sDura2 += ":" + (sec2 < 10 ? "0"+ sec2 : sec2);

                        myViewHolder.tv_TempoAudio.setText(sDura2);

                        Glide.with(myViewHolder.iVIcon_Img.getContext()).load(R.drawable.wave).into(myViewHolder.iVIcon_Img);

                        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 55, 0, 0);
                        myViewHolder.iVIcon_Img.setLayoutParams(params);

                        myViewHolder.descricao.setVisibility(View.GONE);
                        myViewHolder.tv_TempoAudio.setVisibility(View.VISIBLE);
                        myViewHolder.iVIcon_Img.setVisibility(View.VISIBLE);

                    }catch (Exception e){
                        e.printStackTrace();
                        //Toast.makeText(myViewHolder.itemView.getContext(),"Erro",Toast.LENGTH_SHORT).show();

                    }

                }

            }

        try {
            // edita a tarefa ou envia para senha
            myViewHolder.editar.setOnClickListener(v -> {

                if (tarefa.getComSenha().equals("1")) {

                    Intent intent = new Intent(v.getContext(), SenhaActivity.class);
                    intent.putExtra("tarefaSelecionada", tarefa);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);


                } else if (tarefa.getComSenha().equals("0")) {

                    if (tarefa.getComAudio().equals("0") || tarefa.getComImagem().equals("0")) {

                        Intent intent = new Intent(v.getContext(), EditarNotaActivity.class);
                        intent.putExtra("tarefaSelecionada", tarefa);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);

                    } else if (tarefa.getComImagem().equals("1")) {

                        Toast.makeText(v.getContext(), "Nada aqui", Toast.LENGTH_SHORT).show();

                    } else if (tarefa.getComAudio().equals("1")) {

                        Toast.makeText(v.getContext(), "Nada aqui", Toast.LENGTH_SHORT).show();

                    }
                }

            });


            myViewHolder.editar.setOnLongClickListener(v -> {

                try {

                    Vibrator rr = (Vibrator) myViewHolder.itemView.getContext().getSystemService(ContextWrapper.VIBRATOR_SERVICE);
                    long milliseconds = 30;
                    rr.vibrate(milliseconds);

                    View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_adapter_note, (ViewGroup) v,false);
                    dialog = new BottomSheetDialog(myViewHolder.itemView.getContext(), R.style.BaseBottomSheetDialog);
                    dialog.setContentView(view);

                    LinearLayout ll_trancar_note = dialog.findViewById(R.id.ll_trancar_note), ll_excluir_note = dialog.findViewById(R.id.ll_excluir_note), ll_compart_note = dialog.findViewById(R.id.ll_compart_note);

                    if(tarefa.getComSenha().equals("1")) {//se esta com senha ele bloqueia o conteudo
                        assert ll_compart_note != null;
                        ll_compart_note.setVisibility(View.GONE);
                        assert ll_trancar_note != null;
                        ll_trancar_note.setVisibility(View.GONE);
                    }

                    assert ll_trancar_note != null;
                    ll_trancar_note.setOnClickListener(v13 -> {

                        Intent intent = new Intent(myViewHolder.itemView.getContext(), SenhaActivity.class);
                        intent.putExtra("senha",tarefa);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        myViewHolder.itemView.getContext().startActivity(intent);

                        dialog.dismiss();

                    });


                    assert ll_compart_note != null;
                    ll_compart_note.setOnClickListener(v1 -> {


                        if (tarefa.getComAudio().equals("0") || tarefa.getComImagem().equals("0")) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, fromHtml(tarefa.getNomeDescricao()));
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            myViewHolder.itemView.getContext().startActivity(shareIntent);


                        }else if(tarefa.getComImagem().equals("1")){

                            String path = tarefa.getCaminhoImagem();

                            Uri uri = Uri.parse(path);
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("imagem/*");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            myViewHolder.itemView.getContext().startActivity(Intent.createChooser(share, "Share Image File"));


                        }else if(tarefa.getComAudio().equals("1")){

                            String path = tarefa.getCaminhoAudio();

                            Uri uri = Uri.parse(path);
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("audio/*");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            myViewHolder.itemView.getContext().startActivity(Intent.createChooser(share, "Share Image File"));

                        }

                        dialog.dismiss();

                    });

                    assert ll_excluir_note != null;
                    ll_excluir_note.setOnClickListener(v12 -> {

                        if (tarefa.getComAudio().equals("0") || tarefa.getComImagem().equals("0")) {

                            TarefaDAO tarefaDAO = new TarefaDAO(myViewHolder.itemView.getContext());
                            if (tarefaDAO.deletar(tarefa)){
                                listaTarefas.remove(i);
                                this.notifyDataSetChanged();
                                dialog.dismiss();
                                //Toast.makeText(getApplicationContext(),R.string.movidoLixeira,Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(myViewHolder.itemView.getContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();
                            }


                        }else if(tarefa.getComImagem().equals("1")){

                            String path = tarefa.getCaminhoImagem();

                            TarefaDAO tarefaDAO = new TarefaDAO(myViewHolder.itemView.getContext());
                            if (tarefaDAO.deletar(tarefa)){

                                try {
                                    //path = "caminho da pasta/arquivo, etc";
                                    boolean p = new File(path).delete();

                                    if (p) {
                                        listaTarefas.remove(i);
                                        this.notifyDataSetChanged();
                                        dialog.dismiss();
                                        //Toast.makeText(EditarImagemActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();

                                    } else {

                                        Toast.makeText(myViewHolder.itemView.getContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else{
                                Toast.makeText(myViewHolder.itemView.getContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();
                            }

                        }else if(tarefa.getComAudio().equals("1")){
                            String path = tarefa.getCaminhoAudio();

                            TarefaDAO tarefaDAO = new TarefaDAO(myViewHolder.itemView.getContext());
                            if (tarefaDAO.deletar(tarefa)){

                                try {
                                    //path = "caminho da pasta/arquivo, etc";
                                    boolean p = new File(path).delete();

                                    if(p){

                                        listaTarefas.remove(i);
                                        this.notifyDataSetChanged();
                                        dialog.dismiss();
                                        //Toast.makeText(NotasExcluidosActivity.this, "Deletado com sucesso!", Toast.LENGTH_SHORT).show();
                                    }else{

                                        Toast.makeText(myViewHolder.itemView.getContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();

                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }else{
                                Toast.makeText(myViewHolder.itemView.getContext(),R.string.algoDeuErrado,Toast.LENGTH_SHORT).show();
                            }

                        }

                    });

                    dialog.show();

                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;
            });


            //colocar cor na lateral
            myViewHolder.editar.setCardBackgroundColor(Color.parseColor(tarefa.getCor()));


        }catch (Exception e){
            e.printStackTrace();
            Log.e("notaAdapter", "algo deu errado no adapter");

        }

    }

    @Override
    public int getItemCount() {
        return this.listaTarefas.size();
    }

    @Override
    public long getItemId (int i) {
        Tarefa tarefa = listaTarefas.get(i);
        return tarefa.getId();
    }

    public final static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tarefa, descricao, tv_TempoAudio;
        ImageView headerIndicator, iVIcon_Img;
        CardView editar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tarefa = itemView.findViewById(R.id.tarefa);
            descricao = itemView.findViewById(R.id.descricao);
            tv_TempoAudio = itemView.findViewById(R.id.tv_TempoAudio);
            headerIndicator = itemView.findViewById(R.id.headerIndicator);
            editar = itemView.findViewById(R.id.bt_Editar);
            iVIcon_Img = itemView.findViewById(R.id.iVIcon_Img);

        }

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
