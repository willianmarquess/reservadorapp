package br.com.reservador;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;

import java.util.UUID;

import br.com.reservador.model.Usuario;
import br.com.reservador.util.FireBase;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText edtNomeUsuario;
    private EditText edtEmailUsuario;
    private EditText edtSenhaUsuario;
    private EditText edtCpfUsuario;
    private RadioButton radioComum;
    private RadioButton radioAdmin;
    private Button btnCadastrar;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private String tipoUsuario = "";

    private FirebaseAuth autenticacao;
    private DatabaseReference referencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        edtNomeUsuario = findViewById(R.id.edtNomeUsuario);
        edtEmailUsuario = findViewById(R.id.edtEmailUsuario);
        edtSenhaUsuario = findViewById(R.id.edtSenhaUsuario);
        edtCpfUsuario = findViewById(R.id.edtCpfUsuario);
        radioComum = findViewById(R.id.radioComum);
        radioAdmin = findViewById(R.id.radioAdmin);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        progressBar = findViewById(R.id.progresBarCadUser);
        radioGroup = findViewById(R.id.radioGroupUsuario);

        autenticacao = FireBase.getAutenticacao();
        referencia = FireBase.getFirebase();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioAdmin:
                        if (radioAdmin.isChecked()){
                            tipoUsuario = "ADMIN";
                        }
                        break;
                    case R.id.radioComum:
                        if (radioComum.isChecked()){
                            tipoUsuario = "COMUM";
                        }
                        break;
                }
            }
        });

    }

    public void cadastrar(){
       if (!validarCampos()){

           progressBar.setVisibility(View.VISIBLE);



            autenticacao.createUserWithEmailAndPassword(edtEmailUsuario.getText().toString().trim(), edtSenhaUsuario.getText().toString().trim())
                    .addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                final Usuario usuario = new Usuario();
                                usuario.setIdUsuario(autenticacao.getUid());
                                usuario.setNomeUsuario(edtNomeUsuario.getText().toString().trim().toUpperCase());
                                usuario.setEmailUsuario(edtEmailUsuario.getText().toString().trim());
                                usuario.setSenhaUsuario(edtSenhaUsuario.getText().toString().trim());
                                usuario.setCpfUsuario(edtCpfUsuario.getText().toString().trim());
                                usuario.setTipoUsuario(tipoUsuario);

                                referencia.child("usuarios").child(usuario.getIdUsuario()).setValue(usuario);
                                Toast.makeText(getApplicationContext(), "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                autenticacao.signOut();

                                startActivity(new Intent(CadastroUsuarioActivity.this, MainActivity.class));

                            }else{
                                String erro = "";
                                try {
                                        throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    erro = "Digite uma senha mais forte, mínimo 8 caracteres, contendo letras e números";
                                }catch (FirebaseAuthInvalidCredentialsException e) {
                                    erro = "E-mail inválido";
                                }catch (FirebaseAuthUserCollisionException e) {
                                    erro = "E=mail já cadastrado no sistema";
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

    public boolean validarCampos(){
        boolean vazio = false;
        if(edtNomeUsuario.getText().toString().isEmpty()){
            edtNomeUsuario.setError("Digite o campo corretamente!");
            vazio = true;
        }
        if(edtEmailUsuario.getText().toString().isEmpty()){
            edtEmailUsuario.setError("Digite o campo corretamente!");
            vazio = true;
        }
        if(edtSenhaUsuario.getText().toString().isEmpty()) {
            edtSenhaUsuario.setError("Digite o campo corretamente!");
            vazio = true;
        }
        if(edtCpfUsuario.getText().toString().isEmpty()) {
            edtCpfUsuario.setError("Digite o campo corretamente!");
            vazio = true;
        }
        return vazio;
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
