package br.com.lsouza.notepad.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.lsouza.notepad.model.Task;

public class TaskModel extends AndroidViewModel{

    private MutableLiveData<List<Task>> tasks = new MutableLiveData<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;
    private String userID;

    public TaskModel(Application application) {
        super(application);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("Tarefas");
        userID = firebaseAuth.getCurrentUser().getUid();
        load();

    }

    public LiveData<List<Task>> getTasks(){
        return tasks;
    }

    private void load() {

        Query query = dbReference.orderByChild("userId").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    List<Task> bs = new ArrayList<>();
                    for(DataSnapshot task : dataSnapshot.getChildren()){
                        Task t = new Task(
                                String.valueOf(task.child("id").getValue()),
                                String.valueOf(task.child("userId").getValue()),
                                String.valueOf(task.child("titulo").getValue()),
                                String.valueOf(task.child("descricao").getValue()),
                                (Boolean)task.child("prioridade").getValue()
                        );

                        bs.add(t);
                    }
                    tasks.setValue(bs);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("ERRO 0001", "Erro ao recuperar tarefa", databaseError.toException());
            }
        });

    }

}
