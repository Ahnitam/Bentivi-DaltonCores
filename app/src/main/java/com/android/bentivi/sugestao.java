package com.android.bentivi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class sugestao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugestao);
        final SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);

        final EditText Exibido = findViewById(R.id.TextoSugestao);
        Button fab = findViewById(R.id.Enviar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                String email = "contato@bentivi.com.br";
                intent.putExtra(Intent.EXTRA_EMAIL, email);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Sugestão de Correção de Cor");
                intent.putExtra(Intent.EXTRA_TEXT, "Cor (RGB) = "+preferences.getString("RGB",null)+"\nNome Exibido: "+preferences.getString("NomeCor",null)+"\nNome Sugerido: "+Exibido.getText());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });
    }
}
