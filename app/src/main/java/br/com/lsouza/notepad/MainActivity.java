package br.com.lsouza.notepad;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.lsouza.notepad.fragment.AboutFragment;
import br.com.lsouza.notepad.fragment.ListFragment;

import static br.com.lsouza.notepad.R.*;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference dbReference;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case id.navigation_home:
                    changeFragment(new ListFragment());
                    return true;
                case id.logout:
                    deslogar();
                    return true;
                case id.about:
                    changeFragment(new AboutFragment());
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        mTextMessage = (TextView) findViewById(id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbReference = database.getReference("Tarefas");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        if(getIntent()!= null && getIntent().getStringExtra("fragment") != null) {

            String toFragment = getIntent().getStringExtra("fragment");
            if(toFragment.equalsIgnoreCase("registerTask")){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                changeFragment(new br.com.lsouza.notepad.fragment.SaveTaskFragment());
            }else if(toFragment.equalsIgnoreCase("listTask")){
                changeFragment(new ListFragment());
            }
        }

    }

    public void deslogar() {
        auth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    private void changeFragment(Fragment fragment){
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(new ListFragment());
        ft.remove(new br.com.lsouza.notepad.fragment.SaveTaskFragment());
        ft.replace(id.frame, fragment);
        ft.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        if(getIntent()!= null && getIntent().getStringExtra("fragment") != null) {
            String toFragment = getIntent().getStringExtra("fragment");
            if(toFragment.equalsIgnoreCase("registerTask")){
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                changeFragment(new ListFragment());
            }
        }


        return true;
    }
}
