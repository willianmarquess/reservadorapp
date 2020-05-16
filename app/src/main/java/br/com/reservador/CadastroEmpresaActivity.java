package br.com.reservador;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import br.com.reservador.model.Empresa;
import br.com.reservador.model.Usuario;
import br.com.reservador.util.FireBase;

public class CadastroEmpresaActivity extends AppCompatActivity {

    private String array_spinner[];
    private Spinner spTipoEmpresa;
    private EditText edtNomeEmpresa;
    private EditText edtcnpjEmpresa;
    private ProgressBar progresBarCadEmpresa;
    private Button btnCadEmpresa;

    private DatabaseReference referencia;
    private FirebaseUser usuarioLogado;

    private AlertDialog alerta;

    private boolean exists = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        array_spinner=new String[3];
        array_spinner[0]="Escola";
        array_spinner[1]="Hospital";
        array_spinner[2]="Empresa Em Geral";


        spTipoEmpresa =  findViewById(R.id.spTipoEmpresa);
        edtNomeEmpresa = findViewById(R.id.edtNomeEmpresa);
        edtcnpjEmpresa = findViewById(R.id.edtCnpjEmpresa);
        progresBarCadEmpresa = findViewById(R.id.progresBarCadEmpresa);
        btnCadEmpresa = findViewById(R.id.btnCadastrarEmpresa);

        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();


        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spTipoEmpresa.setAdapter(adapter);

        btnCadEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

    }


    public void gravar(){

        progresBarCadEmpresa.setVisibility(View.VISIBLE);

                //buscando no banco o usuario pelo id do usuario logado no sistema
                Query query = referencia.child("usuarios").child(usuarioLogado.getUid());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);

                        Empresa empresa = new Empresa();
                        empresa.setIdEmpresa(usuarioLogado.getUid());
                        empresa.setNomeEmpresa(edtNomeEmpresa.getText().toString().trim().toUpperCase());
                        empresa.setCnpjEmpresa(edtcnpjEmpresa.getText().toString());
                        empresa.setTipoEmpresa(spTipoEmpresa.getSelectedItem().toString());

                        empresa.setUsuarioAdministrador(usuario);

                        referencia.child("empresas").child(empresa.getIdEmpresa()).setValue(empresa);

                        Toast.makeText(getApplicationContext(), "Empresa cadastrada com sucesso!", Toast.LENGTH_SHORT).show();

                        progresBarCadEmpresa.setVisibility(View.GONE);

                        startActivity(new Intent(CadastroEmpresaActivity.this, PrincipalActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    public void cadastrar(){
        Query query = referencia.child("empresas").child(usuarioLogado.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    if (!validarCampos()) {
                        dialogCadastrar();
                    }
                }else{
                    if (!validarCampos()) {
                        Log.d("TESTE", "PASSSOU cadastrar");
                        gravar();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean validarCampos(){
        boolean vazio = false;
        if (edtNomeEmpresa.getText().toString().isEmpty()){
            edtNomeEmpresa.setError("Digite o campo corretamente");
            vazio = true;
        }if (edtcnpjEmpresa.getText().toString().isEmpty()){
            edtcnpjEmpresa.setError("Digite o campo corretamente");
            vazio = true;
        }
        return vazio;
    }

    private void dialogCadastrar() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alterar Empresa");
        builder.setMessage("Já existe uma empresa cadastrada no sistema. \nDeseja realmente alterar os dados?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                gravar();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alerta = builder.create();
        alerta.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        progresBarCadEmpresa.setVisibility(View.GONE);
    }
}
