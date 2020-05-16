package br.com.reservador;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.UUID;

import br.com.reservador.model.Empresa;
import br.com.reservador.model.Item;
import br.com.reservador.model.Reserva;
import br.com.reservador.util.FireBase;

public class CadastrarReservaActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView txtDataReserva;
    private EditText edtNomeItemReserva;
    private EditText edtObservacao;
    private EditText edtQuantidadeItemEstoque;
    private EditText edtQuantidadeItemReserva;
    private String idItem;
    private DatabaseReference referencia;
    private FirebaseUser usuarioLogado;
    private Item item;
    private Button btnCadastrarReserva;
    private String idEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_reserva);

        txtDataReserva = findViewById(R.id.txtData);
        edtNomeItemReserva = findViewById(R.id.edtNomeItemReserva);
        edtObservacao = findViewById(R.id.edtObservacaoItemReserva);
        edtQuantidadeItemEstoque = findViewById(R.id.edtQuantidadeitemEstoque);
        edtQuantidadeItemReserva = findViewById(R.id.edtQuantidadeItemReserva);
        btnCadastrarReserva = findViewById(R.id.btnCadastrarReserva);
        edtQuantidadeItemReserva.setText("0");

        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();

        desabilitarCampos();

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        idItem = bundle.getString("idItem");

        atribuirValorCampos(idItem);

        btnCadastrarReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validarCampos()){
                    cadastrarReserva();
                }
            }
        });

        txtDataReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CadastrarReservaActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, ano, mes, dia);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                txtDataReserva.setText(dayOfMonth+" / "+month+" / "+year);
            }
        };

    }
    public void desabilitarCampos(){
        edtNomeItemReserva.setEnabled(false);
        edtObservacao.setEnabled(false);
        edtQuantidadeItemEstoque.setEnabled(false);
    }
    public void atribuirValorCampos(final String idItem){

        Query query = referencia.child("usuarios").child(usuarioLogado.getUid()).child("empresaUsuario");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                idEmpresa = dataSnapshot.getValue().toString();

                referencia.child("empresas").child(idEmpresa).child("itens").child(idItem).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        item = dataSnapshot.getValue(Item.class);

                        edtNomeItemReserva.setText(item.getNomeItem());
                        edtObservacao.setText(item.getObservacaoItem());
                        edtQuantidadeItemEstoque.setText(item.getQuantidadeitem());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean validarCampos(){
        boolean vazio = false;
        if (edtNomeItemReserva.getText().toString().trim().isEmpty()){
            vazio = true;
            edtNomeItemReserva.setError("Digite o campo corretamente");
        }
        if (edtObservacao.getText().toString().trim().isEmpty()){
            vazio = true;
            edtObservacao.setError("Digite os campos corretamente");
        }
        if (edtQuantidadeItemEstoque.getText().toString().trim().isEmpty()){
            vazio = true;
            edtQuantidadeItemEstoque.setError("Digite os campos corretamente");
        }
        if (edtQuantidadeItemReserva.getText().toString().trim().isEmpty()){
            vazio = true;
            edtQuantidadeItemReserva.setError("Digite os campos corretamente");
        }if (txtDataReserva.getText().toString().equals("Selecione a Data")){
            vazio = true;
            Toast.makeText(getApplicationContext(), "Selecione uma data!", Toast.LENGTH_SHORT).show();
        }
        if (Integer.parseInt(edtQuantidadeItemReserva.getText().toString()) > Integer.parseInt(edtQuantidadeItemEstoque.getText().toString())){
            vazio = true;
            edtQuantidadeItemReserva.setError("A quantidade da reserva não pode ser maior que a quantidade em Estoque");
        }
        if (Integer.parseInt(edtQuantidadeItemReserva.getText().toString()) <= 0){
            vazio = true;
            edtQuantidadeItemReserva.setError("Valor inválido");
        }
        return vazio;
    }

    public void cadastrarReserva(){

        Reserva reserva = new Reserva();

        reserva.setIdReserva(UUID.randomUUID().toString());
        reserva.setDataReserva(txtDataReserva.getText().toString().trim());
        reserva.setQuantidadeReserva(edtQuantidadeItemReserva.getText().toString());
        reserva.setIdItem(idItem);
        reserva.setNomeItem(edtNomeItemReserva.getText().toString().trim());
        reserva.setObservacaoItem(edtObservacao.getText().toString().trim());

        referencia.child("reservas").child(txtDataReserva.getText().toString().trim().replace("/", "")).child("empresas").child(idEmpresa).child("itens").child(idItem).setValue(reserva);
        referencia.child("usuarios").child(usuarioLogado.getUid()).child("reservas").child(txtDataReserva.getText().toString().replace("/", "")).child(idItem).setValue(reserva);

        Toast.makeText(getApplicationContext(), "Reserva Cadastrada com sucesso!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(CadastrarReservaActivity.this, PrincipalComumActivity.class));
        finish();
    }
    }
