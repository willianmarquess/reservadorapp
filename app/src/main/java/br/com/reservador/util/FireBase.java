package br.com.reservador.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBase {
    public static DatabaseReference referenciaFireBase;
    public static FirebaseAuth autenticacao;

    public static DatabaseReference getFirebase(){
        if (referenciaFireBase == null){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            referenciaFireBase = FirebaseDatabase.getInstance().getReference();;
        }
        return referenciaFireBase;
    }
    public static  FirebaseAuth getAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
