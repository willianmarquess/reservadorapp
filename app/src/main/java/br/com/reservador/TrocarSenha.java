package br.com.reservador;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.reservador.util.FireBase;

public class TrocarSenha extends AppCompatActivity {

    private EditText edtTrocarSenha;
    private ProgressBar progressBar;
    private FirebaseAuth autenticacao;
    private Button btnTrocarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocar_senha);

        edtTrocarSenha = findViewById(R.id.edtEmailTrocar);
        progressBar = findViewById(R.id.progresBar3);
        btnTrocarSenha = findViewById(R.id.btnResetar);
        autenticacao = FireBase.getAutenticacao();


        btnTrocarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trocarSenha();
            }
        });


    }
    public void trocarSenha(){
        if (validarCampo()) {
            progressBar.setVisibility(View.VISIBLE);
            autenticacao.sendPasswordResetEmail(edtTrocarSenha.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(TrocarSenha.this, "As instruções para trocar a sua senha foram mandadas no seu e-mail, por favor verifique sua caixa de entrada", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(TrocarSenha.this, MainActivity.class));
                                finish();
                            } else {
                                String erro = "";
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    erro = "Usuário não cadastrado";
                                } catch (Exception e) {
                                    erro = "Problemas ao efetuar o login";
                                }
                                Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_LONG).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public boolean validarCampo(){
        if (edtTrocarSenha.getText().toString().isEmpty()){
            edtTrocarSenha.setError("Digite o campo corretamente");
            return false;
        }
        return true;
    }
}
