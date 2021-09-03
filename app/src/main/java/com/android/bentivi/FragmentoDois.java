package com.android.bentivi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentoDois extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmento_dois);

        final SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        SharedPreferences.Editor saveEdit = preferences.edit();
        saveEdit.clear();
        saveEdit.commit();

        Button Acro = findViewById(R.id.Acromatico);
        Button Dicro = findViewById(R.id.Dicromatico);
        Button Tricro = findViewById(R.id.Tricromatico);
        Button Cegueira = findViewById(R.id.Cegueira);
        Button Normal = findViewById(R.id.Normal);

        Acro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Cor = new Intent(FragmentoDois.this, FragmentoTres.class);
                User(preferences,"Acromático");
                startActivity(Cor);
            }
        });
        Dicro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Cor = new Intent(FragmentoDois.this, FragmentoTres.class);
                User(preferences,"Dicromático");
                startActivity(Cor);
            }
        });
        Tricro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Cor = new Intent(FragmentoDois.this, FragmentoTres.class);
                User(preferences,"Tricromático");
                startActivity(Cor);
            }
        });
        Cegueira.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Cor = new Intent(FragmentoDois.this, FragmentoTres.class);
                User(preferences,"Cegueira");
                startActivity(Cor);
            }
        });
        Normal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Cor = new Intent(FragmentoDois.this, FragmentoTres.class);
                User(preferences,"Normal");
                startActivity(Cor);
            }
        });
    }

    private void User(SharedPreferences preferences, String A) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(A, true);
        editor.commit();
    }
}
