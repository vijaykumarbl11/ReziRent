package com.resieasy.rezirent.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.google.firebase.auth.FirebaseAuth;
import com.resieasy.rezirent.R;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        auth= FirebaseAuth.getInstance();

        Handler handler=new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },100);


            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (auth.getCurrentUser() != null) {
                            Intent intent1 = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent1);
                            finishAffinity();

                        } else {
                            Intent intent11 = new Intent(SplashScreenActivity.this, SignInActivity.class);
                            intent11.putExtra("onlysignin","1234");
                            startActivity(intent11);
                            finishAffinity();
                        }

                    }
                }
            };
            thread.start();







    }
}