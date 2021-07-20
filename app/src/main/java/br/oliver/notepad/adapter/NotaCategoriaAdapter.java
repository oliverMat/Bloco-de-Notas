package br.oliver.notepad.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.oliver.notepad.R;
import br.oliver.notepad.model.Tarefa;
import br.oliver.notepad.model.Tarefas_Tabela;
import br.oliver.notepad.util.TarefaDAO;

public class NotaCategoriaAdapter extends RecyclerView.Adapter<NotaCategoriaAdapter.MyViewHolder> {

    private final List<Tarefas_Tabela> listas;
    private int selectedPosition =-1;
    private boolean click = true;


    public NotaCategoriaAdapter(List<Tarefas_Tabela> lis){
        this.listas = lis;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nota_categoria, parent,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {


        Tarefas_Tabela listaTabela = listas.get(position);
        myViewHolder.nometarefa.setText(listaTabela.getNomeTab());

        TarefaDAO tarefaDAO = new TarefaDAO(myViewHolder.itemView.getContext());
        List<Tarefa> listaTarefas = tarefaDAO.listarTabela(listaTabela.getNomeTab());

        if (listaTarefas.size() == 0 ){
            myViewHolder.tV_Padrao.setText("0");
        }else {
            myViewHolder.tV_Padrao.setText(String.valueOf(listaTarefas.size()));
        }

        //marca item selecionado
        if(selectedPosition == position)
            myViewHolder.back_categoria.setBackground(ResourcesCompat.getDrawable(myViewHolder.itemView.getResources(),R.drawable.fundo_redondo_cinza,null));
        else
            myViewHolder.back_categoria.setBackground(ResourcesCompat.getDrawable(myViewHolder.itemView.getResources(),R.drawable.fundo_redondo_preto,null));

        if (!click) {

            click = true;

            //Toast.makeText(myViewHolder.itemView.getContext(),"Erro", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nometarefa, tV_Padrao;
        CoordinatorLayout back_categoria;

        public MyViewHolder(View itemView) {
            super(itemView);

            nometarefa = itemView.findViewById(R.id.nomeTabela);
            tV_Padrao = itemView.findViewById(R.id.tV_Padrao);
            back_categoria = itemView.findViewById(R.id.back_categoria);

        }
    }

    public void cliclado(int posi) {//recupera a posicao do item marcado na activity categoria
        selectedPosition = posi;
        click = false;

    }
}
