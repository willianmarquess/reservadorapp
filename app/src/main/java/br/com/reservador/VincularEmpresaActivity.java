package br.com.reservador;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import br.com.reservador.model.Empresa;
import br.com.reservador.model.Usuario;
import br.com.reservador.util.FireBase;

public class VincularEmpresaActivity extends AppCompatActivity {

    private EditText edtCodigoEmpresa;
    private Button btnVincularEmpresa;

    private DatabaseReference referencia;
    private FirebaseUser usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vincular_empresa);

        edtCodigoEmpresa = findViewById(R.id.edtCodigoEmpresa);
        btnVincularEmpresa = findViewById(R.id.btnVincular);

        referencia = FireBase.getFirebase();
        usuarioLogado = FireBase.getAutenticacao().getCurrentUser();

        btnVincularEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validarCampos()){
                    vincularEmpresa();
                }
            }
        });
    }

    public boolean validarCampos(){
        boolean vazio = false;
        if (edtCodigoEmpresa.getText().toString().trim().isEmpty()){
            edtCodigoEmpresa.setError("Digite o campo corretamente");
            vazio = true;
        }
        return  vazio;
    }

    public void vincularEmpresa(){

        Query query2 = referencia.child("usuarios").child(usuarioLogado.getUid());

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                referencia.child("empresas").child(edtCodigoEmpresa.getText().toString().trim()).child("UsuariosEmpresa").child(usuario.getIdUsuario()).setValue(usuario);
                referencia.child("usuarios").child(usuario.getIdUsuario()).child("empresaUsuario").setValue(edtCodigoEmpresa.getText().toString().trim());
                Toast.makeText(getApplicationContext(), "Vinculo com sucesso!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(VincularEmpresaActivity.this, PrincipalComumActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }});





    }
}
