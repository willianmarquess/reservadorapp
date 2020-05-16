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
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.reservador.util.FireBase;

public class MudarSenhaUsuarioActivity extends AppCompatActivity {

    private FirebaseUser usuario;
    private FirebaseAuth autenticacao;
    private EditText edtSenha;
    private Button btnTrocarSenha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_senha_usuario);

        edtSenha = findViewById(R.id.edtSenhaUp);
        btnTrocarSenha = findViewById(R.id.btnUpdateSenha);
        progressBar = findViewById(R.id.progresBarUpSenha);

        autenticacao = FireBase.getAutenticacao();
        usuario = autenticacao.getCurrentUser();

        btnTrocarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trocarSenha();
            }
        });


    }

    public void trocarSenha() {
        if (validarCampo()){
            progressBar.setVisibility(View.VISIBLE);
            usuario.updatePassword(edtSenha.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MudarSenhaUsuarioActivity.this, PrincipalActivity.class));
                        finish();
                    } else {
                        String erro = "";
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            erro = "Digite uma senha mais forte, mínimo 8 caracteres, contendo letras e números";
                        }catch (Exception e) {
                            erro = "Erro ao efetuar o cadastro";
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
    }
    }

    public boolean validarCampo(){
        if(edtSenha.getText().toString().isEmpty()){
            edtSenha.setError("Digite o campo corretamente");
            return false;
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
