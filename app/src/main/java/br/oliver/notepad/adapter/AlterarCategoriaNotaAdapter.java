package br.oliver.notepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.oliver.notepad.R;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.model.Tarefas_Tabela;
import br.oliver.notepad.util.TarefaDAO;

public class AlterarCategoriaNotaAdapter extends RecyclerView.Adapter<AlterarCategoriaNotaAdapter.MyViewHolder> {

    private final List<Tarefas_Tabela> listas;
    private String selectedPosition;

    public AlterarCategoriaNotaAdapter(List<Tarefas_Tabela> lis){
        this.listas = lis;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alterar_categoria_nota, parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {


        Tarefas_Tabela listaTabela = listas.get(position);
        myViewHolder.nometarefa.setText(listaTabela.getNomeTab());

        TarefaDAO tarefaDAO = new TarefaDAO(myViewHolder.itemView.getContext());
        List<Tarefa> listaTarefas = tarefaDAO.listarTabela(listaTabela.getNomeTab());

        if (listaTarefas.size() == 0 ){
            myViewHolder.tV_Padrao_mudar.setVisibility(View.GONE);
        }else {
            myViewHolder.tV_Padrao_mudar.setText(String.valueOf(listaTarefas.size()));
        }

        if (listaTabela.getNomeTab().equals(selectedPosition)){
            myViewHolder.linearLayoutEditarPerfil.setBackground(ResourcesCompat.getDrawable(myViewHolder.itemView.getResources(),R.color.colorBrancoGelo,null));

        }

    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nometarefa, tV_Padrao_mudar;
        LinearLayout linearLayoutEditarPerfil;



        public MyViewHolder(View itemView) {
            super(itemView);

            nometarefa = itemView.findViewById(R.id.nomeMudarTabela);
            tV_Padrao_mudar = itemView.findViewById(R.id.tV_Padrao_mudar);
            linearLayoutEditarPerfil = itemView.findViewById(R.id.linearLayoutEditarPerfil);

        }
    }

    public void marcar(String nome) {//recupera a posicao do item marcado na activity categoria
        selectedPosition = nome;

    }
}
