package br.com.reservador;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.reservador.model.Reserva;
import br.com.reservador.model.Usuario;
import br.com.reservador.util.FireBase;

public class PrincipalComumActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth autenticacao;
    private TextView txtNomeUsuario;
    private TextView txtEmpresaUsuario;
    private DatabaseReference referencia;
    private FirebaseUser usuarioLogado;
    private List<Reserva> reservas = new ArrayList<>();;
    private ListView listView;
    private ArrayAdapter<Reserva> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_comum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        autenticacao = FireBase.getAutenticacao();
        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();

        txtNomeUsuario = findViewById(R.id.txtNomeUsuarioTelaComum);
        txtEmpresaUsuario = findViewById(R.id.txtNomeEmpresaTelaComum);
        listView = findViewById(R.id.lista_tela_comum);

        pegarDadosUsuario();
        pegarDadosEmpresaUsuario();
        eventoTabela();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            Toast.makeText(getApplicationContext(), "ABRIU", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_logout_comum) {
            autenticacao.signOut();
            Intent intent = new Intent(PrincipalComumActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_trocar_senha_comum) {
            trocarSenha();

        }else if (id == R.id.menu_cad_reserva_comum) {
            Intent intent = new Intent(PrincipalComumActivity.this,ReservaActivity.class);
            startActivity(intent);

        }else if (id == R.id.menu_lista_reserva_comum) {
            Intent intent = new Intent(PrincipalComumActivity.this, MinhasReservasActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.menu_vinculo_empresa) {

            Query query = referencia.child("usuarios").child(usuarioLogado.getUid()).child("empresaUsuario");
            
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()){
                        Intent intent = new Intent(PrincipalComumActivity.this,VincularEmpresaActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Já existe um vinculo de Empresa!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void trocarSenha(){
        Intent intent = new Intent(PrincipalComumActivity.this, MudarSenhaUsuarioActivity.class);
        startActivity(intent);
    }
    public void pegarDadosUsuario(){

        Query query = referencia.child("usuarios").child(usuarioLogado.getUid()).child("nomeUsuario");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nomeUsuario = dataSnapshot.getValue().toString();
                txtNomeUsuario.setText(nomeUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void pegarDadosEmpresaUsuario(){

        Query query = referencia.child("usuarios").child(usuarioLogado.getUid()).child("empresaUsuario");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String idEmpresa = dataSnapshot.getValue().toString();

                    Query query = referencia.child("empresas").child(idEmpresa).child("nomeEmpresa");

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String nomeEmpresa = dataSnapshot.getValue().toString();
                                txtEmpresaUsuario.setText(nomeEmpresa);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
                    arrayAdapter = new ArrayAdapter<Reserva>(PrincipalComumActivity.this, android.R.layout.simple_list_item_1, reservas);
                    listView.setAdapter(arrayAdapter);

                }else{
                    listView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
