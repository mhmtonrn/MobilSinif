package com.softengine.dudu.mobilsinif;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.RunnableFuture;

public class Splash extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler=new Handler();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        };
        handler.postDelayed(runnable,4000);
    }

    private void startMainActivity() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
