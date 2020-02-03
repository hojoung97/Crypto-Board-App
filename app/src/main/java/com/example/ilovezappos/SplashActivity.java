package com.example.ilovezappos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Splash Screen
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
