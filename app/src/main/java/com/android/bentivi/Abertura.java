package com.android.bentivi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class Abertura extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abertura);

        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);

        if (preferences.contains("IniciouAPP")) {
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Inicial();
                }
            }, 1700);
        }
        else{
                BoasVindas();
            }

        }
    private void Inicial() {
        Intent intent = new Intent(Abertura.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void BoasVindas(){
        Intent intent = new Intent(Abertura.this, FragmentoUm.class);
        startActivity(intent);
        finish();
    }
}
