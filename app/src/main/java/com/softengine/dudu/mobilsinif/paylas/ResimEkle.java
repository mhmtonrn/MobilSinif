package com.softengine.dudu.mobilsinif.paylas;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softengine.dudu.mobilsinif.R;

public class ResimEkle extends AppCompatActivity {
    private Button mEkle;
    private EditText mBaslik,mAciklama,mDers;
    private ImageView mResim;
    private Uri imageuri;
    String ad = null;
    private static final int GALLERY=1;

    private StorageReference mHafiza;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseKullanici;
    private FirebaseAuth mKullanici;

    public ResimEkle() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim_ekle);


        Firebase.setAndroidContext(this);
        mKullanici = FirebaseAuth.getInstance();
        mHafiza= FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Resimler");
        mDatabaseKullanici= FirebaseDatabase.getInstance().getReference().child("Kullanici");



        mEkle=(Button)findViewById(R.id.img_ekle);
        mBaslik= (EditText) findViewById(R.id.img_baslik_ekle);
        mAciklama= (EditText) findViewById(R.id.img_aciklama_ekle);
        mDers= (EditText) findViewById(R.id.img_ders_ekle);
        mResim= (ImageView) findViewById(R.id.img_resim_ekle);

        mResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY);
            }
        });

        mEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arayuzuYukle();
                yuklemeBaslat();
            }
        });

    }


    private void yuklemeBaslat() {

        final String baslik=mBaslik.getText().toString().trim();
        final String aciklama=mAciklama.getText().toString().trim();
        final String ders=mDers.getText().toString().trim();


        if (!TextUtils.isEmpty(baslik)&&!TextUtils.isEmpty(aciklama)&&!TextUtils.isEmpty(ders)&&imageuri!=null){
            StorageReference dosyaYolu=mHafiza.child("Resimler").child(imageuri.getLastPathSegment());
            dosyaYolu.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri uri=taskSnapshot.getDownloadUrl();
                    DatabaseReference yeniResim=mDatabase.push();
                    yeniResim.child("Baslik").setValue(baslik);
                    yeniResim.child("Aciklama").setValue(aciklama);
                    yeniResim.child("Ders").setValue(ders);
                    yeniResim.child("Ad").setValue(ad);
                    yeniResim.child("Image").setValue(uri.toString());
                    startActivity(new Intent(getApplicationContext(), PaylasAnaSayfa.class));
                    finish();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY&&resultCode==RESULT_OK){
            imageuri=data.getData();
            mResim.setImageURI(imageuri);
        }
    }


    private void arayuzuYukle() {
        final String kullanici_id=mKullanici.getCurrentUser().getUid();

        DatabaseReference okulno=mDatabaseKullanici.child(kullanici_id).child("isim");
         okulno.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               ad = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ResimEkle.this, "kullanıcı yok", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
