package com.softengine.dudu.mobilsinif;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Giris extends AppCompatActivity {

    private FirebaseAuth kullanici;
    private DatabaseReference firebaseDatabase;


    private Button girisButton,kayitButton;
    private EditText email,sifre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        kullanici=FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Kullanici");

        email= (EditText) findViewById(R.id.loginEmailField);
        sifre= (EditText) findViewById(R.id.loginPassField);
        kayitButton= (Button) findViewById(R.id.LoginNewaccount);
        kayitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*startActivity(new Intent(getApplicationContext(),Kaydol.class));*/
                Intent amac=new Intent(getApplicationContext(),Kaydol.class);
                startActivity(amac);
            }
        });
        girisButton= (Button) findViewById(R.id.LoginButton);
        girisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Giris.this, "Giriş Deneniyor...", Toast.LENGTH_SHORT).show();
                girisiDogrulama();
            }
        });

    }

    private void girisiDogrulama() {
        String email=this.email.getText().toString().trim();
        String sifre=this.sifre.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(sifre)){
            kullanici.signInWithEmailAndPassword(email,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        kullaniciKontrolu();
;                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Giris.this, "Hata", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Kaydol.class));
                }
            });
        }
    }

    private void kullaniciKontrolu() {
        final String kullaniciId=kullanici.getCurrentUser().getUid();
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(kullaniciId)){
                    Intent gecis=new Intent(Giris.this,MainActivity.class);
                    gecis.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gecis);
                }else{
                    Toast.makeText(Giris.this, "Yeni Hesap Oluştur", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Kaydol.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}