package com.softengine.dudu.mobilsinif.dosyapaylas;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.common.internal.DowngradeableSafeParcel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softengine.dudu.mobilsinif.R;
import com.softengine.dudu.mobilsinif.Sifreleme.Sifrele;
import com.softengine.dudu.mobilsinif.paylas.PaylasAnaSayfa;
import com.softengine.dudu.mobilsinif.paylas.PaylasModel;
import com.softengine.dudu.mobilsinif.paylas.ResimEkle;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class DosyaPaylasAnaSayfa extends AppCompatActivity {

    private static Context context;
    private RecyclerView mBlogList;
    public DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosya_paylas_ana_sayfa);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Dosyalar");
        context=getApplicationContext();
        mBlogList= (RecyclerView) findViewById(R.id.dosya_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));


        if (mDatabase==null){
            Toast.makeText(this, "veri tabanı hatası", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseRecyclerAdapter<DosyaPaylasModel, DosyaPaylasAnaSayfa.PaylasHolder> adapter = new FirebaseRecyclerAdapter<DosyaPaylasModel, PaylasHolder>(
                    DosyaPaylasModel.class,
                    R.layout.resim_row,
                    DosyaPaylasAnaSayfa.PaylasHolder.class,
                    mDatabase
            ) {
                @Override
                protected void populateViewHolder(DosyaPaylasAnaSayfa.PaylasHolder viewHolder, DosyaPaylasModel model, int position) {
                    /*viewHolder.setImage(getApplicationContext(), model.getImage());*/
                    viewHolder.setButton(model.getImage());
                    viewHolder.setBaslik(model.getBaslik());
                    viewHolder.setAciklama(model.getAciklama());
                    viewHolder.setKullanici(new Sifrele().kilitAc(model.getAd()));
                    viewHolder.setDers(model.getDers());

                }


            };

            mBlogList.setAdapter(adapter);

        }

    }


    public static class PaylasHolder extends RecyclerView.ViewHolder {
        View mView;
        public PaylasHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setBaslik(String baslik){
            TextView tBaslik= (TextView) mView.findViewById(R.id.row_img_baslik);
            tBaslik.setText(baslik);
        }

        public void setAciklama(String aciklama){
            TextView tAciklama= (TextView) mView.findViewById(R.id.row_img_aciklama);
            tAciklama.setText(aciklama);
        }

        public void setKullanici(String kullanici){
            TextView tKullanici= (TextView) mView.findViewById(R.id.row_img_kullanici);
            tKullanici.setText(kullanici);
        }
        public void setDers(String ders){
            TextView tDers= (TextView) mView.findViewById(R.id.row_img_ders);
            tDers.setText(ders);
        }

        public void setImage(final Context context, final String image){
            final ImageView tImage= (ImageView) mView.findViewById(R.id.row_img_image);
            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(tImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(image).into(tImage);
                }
            });
        }
        public void setButton(final String link){
            final Button indirButton= (Button) mView.findViewById(R.id.row_img_indir);
            indirButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mView.getContext(), "İndiriliyor", Toast.LENGTH_SHORT).show();
                    Intent inIntent=new Intent(context.getApplicationContext(),Indir.class);
                    inIntent.putExtra("link",link);
                    mView.getContext().startActivity(inIntent);
                }

            });


        }

    }






















    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dosya_paylas_ana_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==R.id.paylass){
            startActivity(new Intent(this,DosyaEkle.class));

        }
        return super.onOptionsItemSelected(item);
    }
}
