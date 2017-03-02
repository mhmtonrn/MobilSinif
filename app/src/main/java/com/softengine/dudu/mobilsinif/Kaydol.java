package com.softengine.dudu.mobilsinif;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softengine.dudu.mobilsinif.Sifreleme.Sifrele;

public class Kaydol extends AppCompatActivity {

    private EditText isim,email,sifre,okulno;
    private Button girisButonu;

    private FirebaseAuth firebaseGiris;
    private DatabaseReference firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);

        firebaseGiris=FirebaseAuth.getInstance();//firebasedeki geçerli kullanıcıyı alır
        firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Kullanici");

        isim= (EditText) findViewById(R.id.nameField);
        email= (EditText) findViewById(R.id.eMailField);
        sifre = (EditText) findViewById(R.id.passField);
        okulno= (EditText) findViewById(R.id.okulNo);
        girisButonu= (Button) findViewById(R.id.kayitButton);
        girisButonu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisikontrolet();


            }
        });

    }

    private void girisikontrolet() {
        final String isim=this.isim.getText().toString().trim();
        final String email=this.email.getText().toString().trim();
        final String sifre=this.sifre.getText().toString().trim();
        final String okulno=this.okulno.getText().toString().trim();

        if (!TextUtils.isEmpty(isim) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(sifre)&& !TextUtils.isEmpty(okulno)){
            firebaseGiris.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String kullaniciId=firebaseGiris.getCurrentUser().getUid();//geçerli yani aktif kullanıcının id değerini aldık
                        DatabaseReference kullaniciDataid= firebaseDatabase.child(kullaniciId);
                        kullaniciDataid.child("isim").setValue(new Sifrele().kilitle(isim));
                        kullaniciDataid.child("email").setValue(email);
                        kullaniciDataid.child("sifre").setValue(sifre);
                        kullaniciDataid.child("okulno").setValue(okulno);

                        Intent yol=new Intent(Kaydol.this, MainActivity.class);
                        yol.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(yol);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Kaydol.this, "Beklenmedik Hata", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
