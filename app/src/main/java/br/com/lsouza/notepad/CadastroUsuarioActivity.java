package br.com.lsouza.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static br.com.lsouza.notepad.R.id;
import static br.com.lsouza.notepad.R.layout;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText inputEmail, inputSenha;
    private Button btnSignUp;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_cadastro_usuario);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        auth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(id.sign_up_button);

        inputEmail = findViewById(id.email);
        inputSenha = findViewById(id.password);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String senha = inputSenha.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Digite um email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(senha)) {
                    Toast.makeText(getApplicationContext(), "Digite uma senha!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (senha.length() < 8) {
                    Toast.makeText(getApplicationContext(), "Senha deve conter no minimo 8 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                //create user
                auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                String retorno;
                                if(task.isSuccessful()){
                                    retorno = "foi";
                                } else {
                                    retorno = "não foi";
                                }

                                Toast.makeText(CadastroUsuarioActivity.this, "Usuario " + retorno + " criado com sucesso:" , Toast.LENGTH_SHORT).show();

                                if (!task.isSuccessful()) {
                                    Toast.makeText(CadastroUsuarioActivity.this, "Email ja está sendo utilizado em outra Conta. \n\n" + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CadastroUsuarioActivity.this, task.getResult().getUser().getUid(), Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(CadastroUsuarioActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
