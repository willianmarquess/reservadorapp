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
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.reservador.util.FireBase;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
     private FirebaseAuth autenticacao;
     private ProgressBar progressBar;
     private TextView txtNomeUsuario;
     private TextView txtNomeEmpresa;
    private DatabaseReference referencia;
    private FirebaseUser usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progresBar2);
        txtNomeUsuario = findViewById(R.id.txtNomeUsuarioTela);
        txtNomeEmpresa = findViewById(R.id.txtNomeEmpresaTela);

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

        pegarDadosUsuario();
        pegarDadosEmpresaUsuario();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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

        if (id == R.id.menu_logout) {
            autenticacao.signOut();
            Intent intent = new Intent(PrincipalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_cad_empresa) {
            Intent intent = new Intent(PrincipalActivity.this, CadastroEmpresaActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_list_item) {
            Intent intent = new Intent(PrincipalActivity.this, ListaItemActivity.class);
            startActivity(intent);

        }  else if (id == R.id.menu_cad_item) {
            Intent intent = new Intent(PrincipalActivity.this, CadastrarItemActivity.class);
            startActivity(intent);

        } else if (id == R.id.menu_trocar_senha) {
            trocarSenha();

        }else if(id == R.id.menu_share_empresa){
            compartilharCodigo();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void trocarSenha(){
        Intent intent = new Intent(PrincipalActivity.this, MudarSenhaUsuarioActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
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
        Query query = referencia.child("empresas").child(usuarioLogado.getUid()).child("nomeEmpresa");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nomeEmpresa = dataSnapshot.getValue().toString();
                    txtNomeEmpresa.setText(nomeEmpresa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void compartilharCodigo(){
            String codigo = usuarioLogado.getUid();

            if (!txtNomeEmpresa.getText().toString().trim().equals("NÃO VINCULADO")) {

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                share.putExtra(Intent.EXTRA_SUBJECT, "Código da Empresa :" + txtNomeEmpresa.getText().toString());
                share.putExtra(Intent.EXTRA_TEXT, codigo);

                startActivity(Intent.createChooser(share, "Compartilhar Código!"));
            }else{
                Toast.makeText(getApplicationContext(), "Empresa não cadastrada!", Toast.LENGTH_SHORT).show();
            }
        }
}
