package br.oliver.notepad.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.oliver.notepad.R;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.model.Tarefa_Lixeira;
import br.oliver.notepad.model.Tarefas_Tabela;
import br.oliver.notepad.util.RecyclerItemClickListener;
import br.oliver.notepad.util.TarefaDAO;
import br.oliver.notepad.util.TarefaDAO_Lixeira;
import br.oliver.notepad.util.TarefaDAO_Tabela;


public class NotasExcluidosAdapter extends RecyclerView.Adapter<NotasExcluidosAdapter.MyViewHolder> {

    private final List<Tarefa_Lixeira> list;

    private LixeiraAdapterListener listener;

    private Context context;

    private AlterarCategoriaNotaAdapter tarefaTabelaAdapter;
    private List<Tarefas_Tabela> listaTarefas_tabelas = new ArrayList<>();
    private Tarefas_Tabela tabelaSelecionada;

    private AlertDialog dialogPrin;

    public final SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int currentSelectedPos;

    public NotasExcluidosAdapter(List<Tarefa_Lixeira> list){
        this.list = list;
    }

    public void setListener(LixeiraAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLsita = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nota,parent,false);
        return new MyViewHolder(itemLsita);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int i) {

        final Tarefa_Lixeira lixeira = list.get(i);
        holder.bind(lixeira);

        context = holder.itemView.getContext();

        holder.tarefa_lixeira.setText(lixeira.getNomeTarefa_lixeira());


        if (lixeira.getComImagem_lixeira().equals("1")) {

            Glide.with(holder.headerIndicator_lixeira.getContext()).load(R.drawable.ic_galeria).into(holder.headerIndicator_lixeira);

            Glide.with(holder.itemView.getContext()).load(lixeira.getCaminhoImagem_lixeira()).into(holder.iVIcon_Img_lixeira);

            ViewGroup.LayoutParams params = holder.iVIcon_Img_lixeira.getLayoutParams();
            // Changes the height and width to the specified *pixels*
            params.height = 600;
            //params.width = 200;
            holder.iVIcon_Img_lixeira.setLayoutParams(params);
            holder.iVIcon_Img_lixeira.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.iVIcon_Img_lixeira.setClipToOutline(true);

            holder.descricao_lixeira.setVisibility(View.GONE);
            holder.iVIcon_Img_lixeira.setVisibility(View.VISIBLE);
            holder.tv_TempoAudio_lixeira.setVisibility(View.GONE);

        }else if (lixeira.getComAudio_lixeira().equals("1")){

            Glide.with(holder.headerIndicator_lixeira.getContext()).load(R.drawable.ic_mic).into(holder.headerIndicator_lixeira);

                    try{

                        MediaPlayer mediaPlayer = MediaPlayer.create(holder.itemView.getContext(), Uri.parse(lixeira.getCaminhoAudio_lixeira()));
                        int duration  = mediaPlayer.getDuration()/1000; // In milliseconds

                        long aux2 ;
                        int min2, sec2;
                        aux2 = duration;

                        min2 = (int) (aux2 / 60);
                        sec2 = (int) (aux2 % 60);

                        String sDura2 = min2 < 10 ? "0"+min2 : min2 + "";

                        sDura2 += ":" + (sec2 < 10 ? "0"+ sec2 : sec2);

                        holder.tv_TempoAudio_lixeira.setText(sDura2);

                        Glide.with(holder.iVIcon_Img_lixeira.getContext()).load(R.drawable.wave).into(holder.iVIcon_Img_lixeira);

                        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 55, 0, 0);
                        holder.iVIcon_Img_lixeira.setLayoutParams(params);

                        holder.descricao_lixeira.setVisibility(View.GONE);
                        holder.iVIcon_Img_lixeira.setVisibility(View.VISIBLE);
                        holder.tv_TempoAudio_lixeira.setVisibility(View.VISIBLE);

                    }catch (Exception e){
                        e.printStackTrace();

                    }

        }else if (lixeira.getComAudio_lixeira().equals("0") || lixeira.getComImagem_lixeira().equals("0")){

            Glide.with(holder.headerIndicator_lixeira.getContext()).load(R.drawable.ic_text).into(holder.headerIndicator_lixeira);
            holder.descricao_lixeira.setText(fromHtml(lixeira.getNomeDescricao_lixeira()));

            holder.descricao_lixeira.setVisibility(View.VISIBLE);
            holder.iVIcon_Img_lixeira.setVisibility(View.GONE);
            holder.tv_TempoAudio_lixeira.setVisibility(View.GONE);
        }



        holder.itemView.setOnClickListener(view -> {

            if (selectedItems.size() > 0 && listener != null){
                listener.onItemClick(i);

            }else {

                try {

                    TarefaDAO_Tabela tarefaDAO_tabela = new TarefaDAO_Tabela(holder.itemView.getContext());
                    listaTarefas_tabelas = tarefaDAO_tabela.listar();

                    //Configurando um adapter da tabela
                    tarefaTabelaAdapter = new AlterarCategoriaNotaAdapter(listaTarefas_tabelas);


                    AlertDialog.Builder dialo = new AlertDialog.Builder(holder.itemView.getContext(), R.style.DialogStyle);
                    dialo.setTitle(holder.itemView.getContext().getString(R.string.restaura));
                    LayoutInflater inflater = (LayoutInflater) holder.itemView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View convertView = inflater.inflate(R.layout.recyclerview_categoria_dialog, null);

                    RecyclerView list = convertView.findViewById(R.id.recyclerTabela);
                    list.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));

                    list.setAdapter(tarefaTabelaAdapter);
                    dialo.setView(convertView); // setView

                    list.addOnItemTouchListener(new RecyclerItemClickListener(holder.itemView.getContext(), list, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                            //Recuperando tarefa para edicao
                            tabelaSelecionada = listaTarefas_tabelas.get(position);

                            restaurar(lixeira, tabelaSelecionada.getNomeTab());

                            dialogPrin.dismiss();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    }));

                    dialo.setNegativeButton(R.string.cancelar, null);

                    dialogPrin = dialo.create();

                    dialogPrin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogPrin.show();


                }catch (Exception ignored){

                }

            }

        });

        holder.itemView.setOnLongClickListener(view -> {
            if (listener != null)
                listener.onItemLongClick(i);
            return true;
        });

        if (currentSelectedPos == i) currentSelectedPos = -1;

    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }
    public List<Tarefa_Lixeira> getSelected() {//acumula todos os itens seleciondos e manda para a activity
        List<Tarefa_Lixeira> selected = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelected()) {
                selected.add(list.get(i));
            }
        }
        return selected;
    }

    public void toggleSelection(int position) {
        currentSelectedPos = position;
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
            list.get(position).setSelected(false);
        } else {
            selectedItems.put(position, true);
            list.get(position).setSelected(true);
        }
        notifyItemChanged(position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tarefa_lixeira, descricao_lixeira, tv_TempoAudio_lixeira;
        ImageView headerIndicator_lixeira, iVIcon_Img_lixeira;
        CardView editar_lixeira;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tarefa_lixeira = itemView.findViewById(R.id.tarefa);
            descricao_lixeira = itemView.findViewById(R.id.descricao);
            tv_TempoAudio_lixeira = itemView.findViewById(R.id.tv_TempoAudio);
            iVIcon_Img_lixeira = itemView.findViewById(R.id.iVIcon_Img);
            headerIndicator_lixeira = itemView.findViewById(R.id.headerIndicator);
            editar_lixeira = itemView.findViewById(R.id.bt_Editar);
        }

        void bind(Tarefa_Lixeira lixeira) {

            if (lixeira.isSelected()) {
                editar_lixeira.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));
            } else {

                editar_lixeira.setCardBackgroundColor(Color.parseColor(lixeira.getCor_lixeira()));
            }

        }
    }

    public interface LixeiraAdapterListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
    
    public void restaurar(Tarefa_Lixeira t, String nome ){

        try {
            Date dataHoraAtual = new Date();
            Date HoraAtual = new Date();
            @SuppressLint("SimpleDateFormat") String dataCriacao = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
            @SuppressLint("SimpleDateFormat") String horCriacao = new SimpleDateFormat("HH:mm").format(HoraAtual);

            //Executar acao para salvar item no DB
            TarefaDAO tarefaDAO = new TarefaDAO(context);

            Tarefa tarefa = new Tarefa();
            tarefa.setId(t.getId_lixeira());
            tarefa.setNomeTabela(nome);
            tarefa.setNomeTarefa(t.getNomeTarefa_lixeira());
            tarefa.setNomeDescricao(t.getNomeDescricao_lixeira());
            tarefa.setCor(t.getCor_lixeira());
            tarefa.setComSenha("0");
            tarefa.setSenhaNota("");
            tarefa.setDataCriacao(dataCriacao);
            tarefa.setHoraCriacao(horCriacao);
            tarefa.setComAudio(t.getComAudio_lixeira());
            tarefa.setCaminhoAudio(t.getCaminhoAudio_lixeira());
            tarefa.setComImagem(t.getComImagem_lixeira());
            tarefa.setCaminhoImagem(t.getCaminhoImagem_lixeira());
            tarefa.setFavorito("0");

            if (tarefaDAO.salvar(tarefa)) {

                TarefaDAO_Lixeira tarefaDAOLixeira =new TarefaDAO_Lixeira(context);
                if ((tarefaDAOLixeira.deletar(t))) {
                    ((Activity)context).finish();
                    //Toast.makeText(getApplicationContext(),"Excluida!",Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(context,"Algo de errado aconteceu...",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Algo de errado aconteceu...", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
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
