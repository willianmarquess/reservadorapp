package br.com.reservador;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import br.com.reservador.model.Item;
import br.com.reservador.util.FireBase;

public class AlterarItemActivity extends AppCompatActivity {

    private Spinner spStatusItem;
    private EditText edtNomeItem;
    private EditText edtObservacaoItem;
    private EditText edtQuantidadeItem;
    private Button btnAlterarItem;
    private ProgressBar progressBar;
    private FirebaseUser usuarioLogado;
    private String idItem;

    private String array_spinner[];

    private DatabaseReference referencia;

    private AlertDialog alerta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_item);

        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        idItem = bundle.getString("idItem");


        array_spinner=new String[2];
        array_spinner[0]="ATIVO";
        array_spinner[1]="INATIVO";

        edtNomeItem = findViewById(R.id.edtNomeItemAtt);
        edtObservacaoItem = findViewById(R.id.edtObservacaoItemAtt);
        edtQuantidadeItem = findViewById(R.id.edtQuantidadeitemAtt);
        btnAlterarItem = findViewById(R.id.btnAlterarItem);
        progressBar = findViewById(R.id.progresBarAlteraritem);
        spStatusItem = findViewById(R.id.spStatusItemAtt);

        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, array_spinner);
        spStatusItem.setAdapter(adapter);

        btnAlterarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCampos()) {
                    alterarItem();
                }
            }
        });

    }

    public void alterarItem(){
        progressBar.setVisibility(View.VISIBLE);

        Item item = new Item();

        item.setIdItem(idItem);
        item.setNomeItem(edtNomeItem.getText().toString().trim().toUpperCase());
        item.setObservacaoItem(edtObservacaoItem.getText().toString().trim().toUpperCase());
        item.setQuantidadeitem(edtQuantidadeItem.getText().toString().trim());
        item.setStatusItem(spStatusItem.getSelectedItem().toString());

        referencia.child("empresas").child(usuarioLogado.getUid()).child("itens").child(item.getIdItem()).setValue(item);

        Toast.makeText(getApplicationContext(), "Item alterado com sucesso!", Toast.LENGTH_SHORT).show();


        progressBar.setVisibility(View.GONE);

        startActivity(new Intent(AlterarItemActivity.this, ListaItemActivity.class));
        finish();

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
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

}
