package com.android.bentivi;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity{
    SharedPreferences preferences;
    Button CameraDetectar,Dinheiro;
    LinearLayout Top;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Criando variavel do botão
        CameraDetectar = findViewById(R.id.Camera);
        Dinheiro = findViewById(R.id.Money);
        Top = findViewById(R.id.topPanel);
        preferences = getSharedPreferences("User", MODE_PRIVATE);
        // Chamar outra Tela quando clickar no botão
        VerificarUser();

        CameraDetectar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent Cor = new Intent(MainActivity.this, ActivityDetectarCor.class);
                startActivity(Cor);
            }
        });
        Dinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Funcionalidade será adicionada no futuro", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


            }
            public void VerificarUser(){
                if (preferences.contains("Acromático")) {
                    Top.setBackgroundResource(R.color.BackAcro);
                    CameraDetectar.setBackgroundTintList(getResources().getColorStateList(R.color.BackButtonAcro));
                    Dinheiro.setBackgroundTintList(getResources().getColorStateList(R.color.BackButtonAcro));

                    TextView A= findViewById(R.id.Lo);
                    A.setTextColor(getResources().getColor(R.color.FontCorPreto));

                    Drawable imagemC = getResources().getDrawable(R.drawable.camerablack);
                    CameraDetectar.setCompoundDrawablesWithIntrinsicBounds(null,imagemC,null,null);
                    CameraDetectar.setTextColor(getResources().getColor(R.color.FontCorPreto));
                    Drawable imagemD = getResources().getDrawable(R.drawable.moneyblack);
                    Dinheiro.setCompoundDrawablesWithIntrinsicBounds(null,imagemD,null,null);
                    Dinheiro.setTextColor(getResources().getColor(R.color.FontCorPreto));
                }
                if (preferences.contains("Dicromático")) {
                    Top.setBackgroundResource(R.color.BackDicro);
                    CameraDetectar.setBackgroundTintList(getResources().getColorStateList(R.color.BackButtonDicro));
                    Dinheiro.setBackgroundTintList(getResources().getColorStateList(R.color.BackButtonDicro));
                }
                if (preferences.contains("Tricromático")) {
                    Top.setBackgroundResource(R.color.BackTrico);
                    CameraDetectar.setBackgroundTintList(getResources().getColorStateList(R.color.BackButtonTrico));
                    Dinheiro.setBackgroundTintList(getResources().getColorStateList(R.color.BackButtonTrico));


                    Drawable imagem = getResources().getDrawable(R.drawable.camera);
                    CameraDetectar.setCompoundDrawablesWithIntrinsicBounds(null,imagem,null,null);
                    CameraDetectar.setTextColor(getResources().getColor(R.color.FontCorBranco));
                    Drawable imagemD = getResources().getDrawable(R.drawable.dinheiro);
                    Dinheiro.setCompoundDrawablesWithIntrinsicBounds(null,imagemD,null,null);
                    Dinheiro.setTextColor(getResources().getColor(R.color.FontCorBranco));
                }
                else{

                }
            }

}
