package com.android.bentivi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentoTres extends AppCompatActivity {
    Button Permi;
    FloatingActionButton Flo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmento_tres);

        Permi = findViewById(R.id.Permissao);
        Flo = findViewById(R.id.Seguin);
        final SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        Permi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(FragmentoTres.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FragmentoTres.this,
                            new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });
        Flo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FragmentoTres.this, MainActivity.class);
                JaIniciou(preferences);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();

        if(ContextCompat.checkSelfPermission(FragmentoTres.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

        }
        else {
            Permi.setVisibility(View.INVISIBLE);
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Flo.show();;
                }
            }, 1200);

        }
    }
    private void JaIniciou(SharedPreferences preferences) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("IniciouAPP", true);
        editor.commit();
    }
}
