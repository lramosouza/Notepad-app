package br.com.lsouza.notepad.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import br.com.lsouza.notepad.MainActivity;
import br.com.lsouza.notepad.R;
import br.com.lsouza.notepad.model.Task;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveTaskFragment extends Fragment {

    private EditText etTitulo, etDescricao;
    private CheckBox chkPrioridade;
    private Button btnSalvar;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private FirebaseAuth auth;
    private String userID;
    private String taskId = null;

    public SaveTaskFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("Tarefas");

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();

        Bundle data = getActivity().getIntent().getExtras();

        Task taskToEdit = null;
        if(data != null){
            taskToEdit = data.getParcelable("obj");
        }

        View view = inflater.inflate(R.layout.fragment_register_task, container, false);

        etTitulo = view.findViewById(R.id.etTitulo);
        etDescricao = view.findViewById(R.id.etDescricao);
        chkPrioridade = view.findViewById(R.id.chkPrioridade);
        btnSalvar = view.findViewById(R.id.btnSalvar);

        if(taskToEdit != null){
            etTitulo.setText(taskToEdit.getTitulo());
            etDescricao.setText(taskToEdit.getDescricao());
            chkPrioridade.setChecked(taskToEdit.getPrioridade());
            taskId = taskToEdit.getId();
        }


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etTitulo.getText().toString();
                String descricao = etDescricao.getText().toString();

                if(titulo == null || titulo.isEmpty()){
                    Toast.makeText(getActivity(), "O titulo deve ser preenchido", Toast.LENGTH_SHORT).show();
                }else if(descricao == null || descricao.isEmpty()){
                    Toast.makeText(getActivity(), "A descrição deve ser preenchida", Toast.LENGTH_SHORT).show();
                }else {
                    String id = null;
                    if(taskId != null){
                        id = taskId;
                    }else {
                        id = dbReference.push().getKey();
                    }


                    Task task = new Task();
                    task.setId(id);
                    task.setUserId(userID);
                    task.setTitulo(etTitulo.getText().toString());
                    task.setDescricao(etDescricao.getText().toString());
                    task.setPrioridade(chkPrioridade.isChecked());

                    dbReference.child(id).setValue(task);

                    Toast.makeText(getActivity(), "Tarefa salva com sucesso", Toast.LENGTH_SHORT).show();

                    clearFields();
                    changeFragment(new ListFragment());
                }

            }
        });

        return view;
    }

    private void clearFields(){
        etTitulo.setText("");
        etDescricao.setText("");
        chkPrioridade.setChecked(false);
    }

    private void changeFragment(Fragment fragment){
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("fragment", "listTasks");
        startActivity(i);
        getActivity().finish();

    }

}
