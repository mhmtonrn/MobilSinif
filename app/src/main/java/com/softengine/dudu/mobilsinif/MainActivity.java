package com.softengine.dudu.mobilsinif;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softengine.dudu.mobilsinif.Sifreleme.Sifrele;
import com.softengine.dudu.mobilsinif.dosyapaylas.DosyaPaylasAnaSayfa;
import com.softengine.dudu.mobilsinif.paylas.PaylasAnaSayfa;
import com.softengine.dudu.mobilsinif.sohbet.SohbetAnaSayfa;

public class MainActivity extends AppCompatActivity {
    private Button mSohbet,mPaylas,mDosyaPaylas;
    private ImageView mProfilResmi;
    private TextView mOkulNo, mAdiSoyadi;

    private FirebaseAuth kullanici;
    private FirebaseAuth.AuthStateListener kullaniciDinleyici;
    private DatabaseReference firebaseDatabase;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        kullanici = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance().getReference().child("Kullanici");

        kullaniciDinleyici= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Toast.makeText(MainActivity.this, "Kullanici oturumu yok giriş yapın", Toast.LENGTH_SHORT).show();
                    Intent gecis=new Intent(getApplicationContext(),Giris.class);//geçiş işlemi
                    gecis.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(gecis);
                }
            }
        };

        mAdiSoyadi= (TextView) findViewById(R.id.ana_sayfa_adi);
        mOkulNo=(TextView)findViewById(R.id.ana_sayfa_ogrenci_no);
        mProfilResmi=(ImageView)findViewById(R.id.ana_sayfa_resim);
        mSohbet=(Button)findViewById(R.id.ana_sayfa_sohbet_ac);
        mDosyaPaylas= (Button) findViewById(R.id.ana_sayfa_dosya_paylas);
        mDosyaPaylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DosyaPaylasAnaSayfa.class));
            }
        });
        mPaylas=(Button)findViewById(R.id.ana_sayfa_paylasma);
        arayuzuYukle();
        mSohbet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SohbetAnaSayfa.class));
            }
        });

        mPaylas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), PaylasAnaSayfa.class));
            }
        });




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void arayuzuYukle() {

        try {

        final String kullanici_id = kullanici.getCurrentUser().getUid();

        DatabaseReference okulno = firebaseDatabase.child(kullanici_id);
        okulno.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mOkulNo.setText(dataSnapshot.child("okulno").getValue(String.class));
                mAdiSoyadi.setText(new Sifrele().kilitAc(dataSnapshot.child("isim").getValue(String.class)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }catch (Exception e){

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        kullanici.addAuthStateListener(kullaniciDinleyici);
        arayuzuYukle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==R.id.cikis){
            kullanici.signOut();

        }
        return super.onOptionsItemSelected(item);
    }
}
