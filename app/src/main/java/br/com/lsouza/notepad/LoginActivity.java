package br.com.lsouza.notepad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static br.com.lsouza.notepad.R.*;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputSenha;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btncadastrar, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(layout.activity_login);

        progressBar = findViewById(id.progressBar);
        inputEmail = findViewById(id.email);
        inputSenha = findViewById(id.senha);
        btnLogin = findViewById(id.btn_login);
        btnReset = findViewById(id.btn_reset_password);
        btncadastrar = findViewById(id.btn_cadastrar);

        auth = FirebaseAuth.getInstance();

        btncadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CadastroUsuarioActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, EsqueciSenhaActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                final String senha  = inputSenha.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(), "Informe um email", Toast.LENGTH_SHORT).show();
                }

                if(TextUtils.isEmpty(senha)){
                    Toast.makeText(getApplicationContext(), "Digite uma senha", Toast.LENGTH_SHORT).show();
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);

                                if(!task.isSuccessful()){
                                    if(senha.length() < 6){
                                        inputSenha.setError(getString(string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(string.auth_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });

            }
        });

    }
}
