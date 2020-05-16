package br.com.reservador;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import br.com.reservador.model.Usuario;
import br.com.reservador.util.FireBase;

public class MainActivity extends AppCompatActivity {

    private TextView txtAbrirCadastro;
    private Button btnLogar;
    private EditText edtEmail;
    private EditText edtSenha;
    private FirebaseAuth autenticacao;
    private ProgressBar progressBar;
    private TextView txtEsquecer;
    private FirebaseUser usuarioLogado;
    private DatabaseReference referencia;
    private String tipoUsuario = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        referencia = FireBase.getFirebase();

        autenticacao = FireBase.getAutenticacao();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();


        if (usuarioLogado != null) {
            Query query = referencia.child("usuarios").child(usuarioLogado.getUid()).child("tipoUsuario");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tipoUsuario = dataSnapshot.getValue().toString();

                    Intent intent = null;

                    if (tipoUsuario.equals("ADMIN")){
                        intent = new Intent(MainActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (tipoUsuario.equals("COMUM")){
                        intent = new Intent(MainActivity.this, PrincipalComumActivity.class);
                        startActivity(intent);
                        finish();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(getApplicationContext(), "usuário logado :"+autenticacao.getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,
                    PrincipalActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);

        txtAbrirCadastro = findViewById(R.id.txtAbrirCadastro);
        btnLogar = findViewById(R.id.btnLogar);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        progressBar = findViewById(R.id.progressBar);
        txtEsquecer = findViewById(R.id.txtEsqueceu);

        limparCampos();

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solicitarPermissoes();
                logar();
            }
        });

        txtAbrirCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastro();
            }
        });
        txtEsquecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTrocarSenha();
            }
        });
    }

    public void abrirCadastro(){
        Intent intent =  new Intent(MainActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
    public void abrirTrocarSenha(){
        Intent intent =  new Intent(MainActivity.this, TrocarSenha.class);
        startActivity(intent);
    }
    public void logar(){
        if (!validarCampos()){
                progressBar.setVisibility(View.VISIBLE);
                autenticacao.signInWithEmailAndPassword(edtEmail.getText().toString().trim(), edtSenha.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);

                            usuarioLogado = FireBase.getAutenticacao().getCurrentUser();

                            Query query = referencia.child("usuarios").child(usuarioLogado.getUid()).child("tipoUsuario");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tipoUsuario = dataSnapshot.getValue().toString();
                                    Intent intent = null;

                                    if (tipoUsuario.equals("ADMIN")){
                                        intent = new Intent(MainActivity.this, PrincipalActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    if (tipoUsuario.equals("COMUM")){
                                        intent = new Intent(MainActivity.this, PrincipalComumActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }else{
                            String erro = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e){
                                erro = "Usuário não cadastrado";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erro = "Senha inválida";
                            }
                            catch (Exception e){
                                erro = "Problemas ao efetuar o login";
                            }
                            Toast.makeText(getApplicationContext(), erro,Toast.LENGTH_LONG).show();

                        }
                        progressBar.setVisibility(View.GONE);

                        }

                });
        }

    }
    public boolean validarCampos(){
        boolean vazio = false;
        if (edtEmail.getText().toString().isEmpty()){
            edtEmail.setError("Digite o campo corretamente");
            vazio = true;
        }if (edtSenha.getText().toString().isEmpty()){
            edtSenha.setError("Digite o campo corretamente");
            vazio = true;
        }
        return vazio;
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public void limparCampos(){
        if (!edtEmail.getText().toString().isEmpty()){
            edtEmail.setText("");
        }
        if (!edtSenha.getText().toString().isEmpty()){
            edtSenha.setText("");
        }
    }

    public void solicitarPermissoes(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report){
                        // checa todas as permissões foram concedidas
                        if (report.areAllPermissionsGranted()) {
                        }
                        // permissão negada, checa se foi negado permanente,

                        // em algum recurso solicitado

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // abre a tela de configuração do app
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).onSameThread()
                .check();
    }

}
