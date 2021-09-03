package com.android.bentivi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FragmentoUm  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragmento);

        final FloatingActionButton A = findViewById(R.id.floatingActionButton);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                A.show();;
            }
        }, 1200);
        A.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent A = new Intent(FragmentoUm.this, FragmentoDois.class);
                startActivity(A);
            }
        });

    }
}
