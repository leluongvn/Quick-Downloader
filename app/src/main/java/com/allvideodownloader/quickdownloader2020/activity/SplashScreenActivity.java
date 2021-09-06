package com.allvideodownloader.quickdownloader2020.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideodownloader.quickdownloader2020.R;

public class SplashScreenActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;


        new Handler(getMainLooper()).postDelayed(() -> {

            finish();
            startActivity(new Intent(context, MainActivity.class));
        }, 2000);

    }

}
