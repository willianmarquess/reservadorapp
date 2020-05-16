package br.com.reservador;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.reservador.model.Item;
import br.com.reservador.model.Usuario;
import br.com.reservador.util.FireBase;

public class ListaItemActivity extends AppCompatActivity {

    private EditText edtNomeItemBusca;
    private ListView listaItens;
    private AlertDialog alerta;

    private DatabaseReference referencia;
    private List<Item> itens = new ArrayList<Item>();
    private List<String> ids =  new ArrayList<String>();
    private ArrayAdapter<Item> arrayAdapter;
    private FirebaseUser usuarioLogado;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_item);

        edtNomeItemBusca = findViewById(R.id.edtNomeitemBusca);
        listaItens = findViewById(R.id.listItens);

        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();


        listaItens.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selecionarItem(position);
            }
        });

        eventoTabela();
    }

    public void eventoTabela(){
        referencia.child("empresas").child(usuarioLogado.getUid()).child("itens").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itens.clear();
                ids.clear();
                for (DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    Item item = objSnapshot.getValue(Item.class);
                    ids.add(item.getIdItem());
                    itens.add(item);
                }

                arrayAdapter = new ArrayAdapter<Item>(ListaItemActivity.this, android.R.layout.simple_list_item_1, itens);
                listaItens.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void selecionarItem(int position){
        String id = ids.get(position);
        dialogCadastrar(id);
    }

    private void dialogCadastrar(final String id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alterar Item");
        builder.setMessage("Deseja ir para tela de alteração dos dados do item?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                Intent intent = new Intent(ListaItemActivity.this, AlterarItemActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("idItem", id);
                intent.putExtras(bundle);

                startActivity(intent);
                finish();

            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });

        alerta = builder.create();
        alerta.show();
    }
}
