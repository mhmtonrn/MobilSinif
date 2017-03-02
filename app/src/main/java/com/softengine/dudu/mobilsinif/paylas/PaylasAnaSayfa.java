package com.softengine.dudu.mobilsinif.paylas;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.softengine.dudu.mobilsinif.R;
import com.softengine.dudu.mobilsinif.Sifreleme.Sifrele;
import com.softengine.dudu.mobilsinif.dosyapaylas.DosyaPaylasAnaSayfa;
import com.softengine.dudu.mobilsinif.dosyapaylas.Indir;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class PaylasAnaSayfa extends AppCompatActivity {
    public static Context context;
    private RecyclerView mBlogList;
    public DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paylas_ana_sayfa);

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Resimler");
        context=getApplicationContext();
        mBlogList= (RecyclerView) findViewById(R.id.resim_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));


        if (mDatabase==null){
            Toast.makeText(this, "veri tabanı hatası", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseRecyclerAdapter<PaylasModel, PaylasHolder> adapter = new FirebaseRecyclerAdapter<PaylasModel, PaylasHolder>(
                    PaylasModel.class,
                    R.layout.resim_row,
                    PaylasHolder.class,
                    mDatabase
            ) {
                @Override
                protected void populateViewHolder(PaylasHolder viewHolder, PaylasModel model, int position) {
                    viewHolder.setImage(getApplicationContext(), model.getImage());
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

    @Override
    protected void onStart() {
        super.onStart();

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
            Picasso.with(context).load(image).into(tImage);

           /* Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(tImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });*/
        }
        public void setButton(final String link){
            Button indirButton= (Button) mView.findViewById(R.id.row_img_indir);
            indirButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mView.getContext(), "İndiriliyor", Toast.LENGTH_SHORT).show();
                    /*Intent inIntent=new Intent(context.getApplicationContext(),Indir.class);
                    inIntent.putExtra("link",link);
                    mView.getContext().startActivity(inIntent);*/

                    String path=link;
                    int file_lenght=0;

                    try {
                        URL url=new URL(path);
                        URLConnection urlConnection =url.openConnection();
                        urlConnection.connect();
                        file_lenght=urlConnection.getContentLength();
                        File newFolder=new File("sdcard/photosalbum");
                        if (!newFolder.exists()){
                            newFolder.mkdir();
                        }
                        File inputFile=new File(newFolder,"dImage.jpg");
                        InputStream inputStream=new BufferedInputStream(url.openStream(),8192);
                        byte[] data=new byte[1024];
                        int total=0;
                        int count=0;
                        OutputStream outputStream=new FileOutputStream(inputFile);
                        while ((count=inputStream.read(data))!=-1){
                            total+=count;
                            outputStream.write(data,0,count);


                        }
                        inputStream.close();
                        outputStream.close();


                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }








                }


            });
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.paylas_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()==R.id.resim_ekle){
            startActivity(new Intent(this,ResimEkle.class));

        }
        return super.onOptionsItemSelected(item);
    }

    static class DTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... params) {


            return "Tamamlandı";
        }


    }
}
