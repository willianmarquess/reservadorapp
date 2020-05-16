package br.com.reservador;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.reservador.model.Empresa;
import br.com.reservador.model.Item;
import br.com.reservador.util.FireBase;

public class CadastrarItemActivity extends AppCompatActivity {

    private Spinner spStatusItem;
    private EditText edtNomeItem;
    private EditText edtObservacaoItem;
    private EditText edtQuantidadeItem;
    private Button btnCadastrarItem;
    private ProgressBar progressBar;
    private FirebaseUser usuarioLogado;

    private String array_spinner[];

    private DatabaseReference referencia;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();

        validarCadastro();


        setContentView(R.layout.activity_cadastrar_item);

        array_spinner=new String[2];
        array_spinner[0]="ATIVO";
        array_spinner[1]="INATIVO";

        spStatusItem = findViewById(R.id.spStatusItem);
        edtNomeItem = findViewById(R.id.edtNomeItem);
        edtObservacaoItem = findViewById(R.id.edtObservacaoItem);
        edtQuantidadeItem = findViewById(R.id.edtQuantidadeItem);
        progressBar = findViewById(R.id.progresBarCadItem);
        btnCadastrarItem = findViewById(R.id.btnCadastrarItem);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spStatusItem.setAdapter(adapter);

        btnCadastrarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCampos()){
                    cadastrarItem();
                }
            }
        });


    }


    public void validarCadastro(){
        Query query = referencia.child("empresas").child(usuarioLogado.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    dialogTelaEmpresa();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void dialogTelaEmpresa(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Aviso!");
        builder.setMessage("Nenhuma empresa está vinculada ao seu usuário \nPor favor, antes de cadastrar um ITEM deve inserir uma empresa! \nDeseja inserir uma empresa?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startActivity(new Intent(CadastrarItemActivity.this, CadastroEmpresaActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                startActivity(new Intent(CadastrarItemActivity.this, PrincipalActivity.class));
                finish();
            }
        });

        alerta = builder.create();
        alerta.show();
    }

    public boolean validarCampos(){
        boolean vazio = false;
        if (edtNomeItem.getText().toString().isEmpty()){
            edtNomeItem.setError("Digite o campo corretamente!");
            vazio = true;
        }
        if (edtQuantidadeItem.getText().toString().isEmpty()){
            edtQuantidadeItem.setError("Digite o campo corretamente!");
            vazio = true;
        }if (edtObservacaoItem.getText().toString().isEmpty()){
            edtObservacaoItem.setError("Digite o campo corretamente!");
            vazio = true;
        }
        return vazio;
    }

    public void cadastrarItem(){
        progressBar.setVisibility(View.VISIBLE);

                Item item = new Item();

                item.setIdItem(UUID.randomUUID().toString());
                item.setNomeItem(edtNomeItem.getText().toString().trim().toUpperCase());
                item.setObservacaoItem(edtObservacaoItem.getText().toString().trim().toUpperCase());
                item.setQuantidadeitem(edtQuantidadeItem.getText().toString().trim());
                item.setStatusItem(spStatusItem.getSelectedItem().toString());

                referencia.child("empresas").child(usuarioLogado.getUid()).child("itens").child(item.getIdItem()).setValue(item);

                Toast.makeText(getApplicationContext(), "Item cadastrado com sucesso!", Toast.LENGTH_SHORT).show();


        progressBar.setVisibility(View.GONE);

        startActivity(new Intent(CadastrarItemActivity.this, PrincipalActivity.class));
        finish();


    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
