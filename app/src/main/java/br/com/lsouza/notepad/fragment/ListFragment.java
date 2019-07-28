package br.com.lsouza.notepad.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.lsouza.notepad.R;
import br.com.lsouza.notepad.MainActivity;
import br.com.lsouza.notepad.model.Task;
import br.com.lsouza.notepad.view.adapter.TaskAdapter;
import br.com.lsouza.notepad.view.listener.OnItemClickListener;
import br.com.lsouza.notepad.viewmodel.TaskModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private FloatingActionButton fab;
    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private List<Task> tasks;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference("Tarefas");

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        fab = v.findViewById(R.id.fab);
        rvTasks = v.findViewById(R.id.rvTasks);
        tasks = new ArrayList<>();

        ViewModelProviders.of(this)
                .get(TaskModel.class)
                .getTasks()
                .observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(@Nullable List<Task> tasks) {
                        adapter.setList(tasks);
                        rvTasks.getAdapter().notifyDataSetChanged();
                    }
                });

        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskAdapter(tasks, deleteClick, editClick);
        rvTasks.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new SaveTaskFragment());
            }
        });

        return v;
    }

    private void changeFragment(Fragment fragment){
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra("fragment", "registerTask");
        startActivity(i);
        getActivity().finish();

    }

    private OnItemClickListener deleteClick = new OnItemClickListener() {
        @Override
        public void onClick(int position) {

            Query query = dbReference.orderByChild("id").equalTo(adapter.getTask(position).getId());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        data.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    };

    private OnItemClickListener editClick = new OnItemClickListener() {
        @Override
        public void onClick(int position) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.putExtra("fragment", "registerTask");
            i.putExtra("obj", adapter.getTask(position));
            startActivity(i);
            getActivity().finish();
        }
    };

}
