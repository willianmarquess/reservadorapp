package br.com.reservador;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.reservador.model.Item;
import br.com.reservador.model.Reserva;
import br.com.reservador.util.FireBase;

public class MinhasReservasActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Reserva> arrayAdapter;
    private FirebaseUser usuarioLogado;
    private DatabaseReference referencia;
    private List<String> valores;
    private List<Reserva> reservas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_reservas);

        listView = findViewById(R.id.listaMinhasReservas);

        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();

        eventoTabela();
    }

    public void eventoTabela(){

        Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        mes += 1;

        referencia.child("usuarios/"+usuarioLogado.getUid()+"/reservas/"+dia+"  "+mes+"  "+ano).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Reserva reserva = objSnapshot.getValue(Reserva.class);
                        reservas.add(reserva);
                    }
                    arrayAdapter = new ArrayAdapter<Reserva>(MinhasReservasActivity.this, android.R.layout.simple_list_item_1, reservas);
                    listView.setAdapter(arrayAdapter);

                }else{
                    Toast.makeText(getApplicationContext(), "Nenhum dado encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
