package br.com.lsouza.notepad.view.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.lsouza.notepad.R;
import br.com.lsouza.notepad.model.Task;
import br.com.lsouza.notepad.view.listener.OnItemClickListener;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;

    private final OnItemClickListener listenerDelete;
    private final OnItemClickListener listenerEdit;

    public TaskAdapter(List<Task> tasks, OnItemClickListener deleteClick, OnItemClickListener editClick) {
        this.tasks = tasks;
        this.listenerDelete = deleteClick;
        this.listenerEdit = editClick;
    }

    public void setList(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_task, parent, false);

        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, final int position) {
        Task task = tasks.get(position);
        holder.txtTitulo.setText(task.getTitulo());
        holder.txtPrioridade.setText("Tarefa prioritária? - " + (task.getPrioridade() ? "Sim": "Não"));

        holder.btApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerDelete.onClick(position);
            }
        });

        holder.btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerEdit.onClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitulo, txtPrioridade;
        ImageView btApagar, btEditar;

        public TaskViewHolder(View v) {
            super(v);

            txtTitulo = v.findViewById(R.id.txtTitulo);
            txtPrioridade = v.findViewById(R.id.txtPrioridade);
            btApagar = v.findViewById(R.id.btApagar);
            btEditar = v.findViewById(R.id.btEditar);
        }
    }

    public Task getTask(int position){
        return tasks.get(position);
    }

}
