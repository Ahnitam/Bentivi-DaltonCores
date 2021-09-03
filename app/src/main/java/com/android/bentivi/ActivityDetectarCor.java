package com.android.bentivi;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class ActivityDetectarCor extends AppCompatActivity implements CameraColorPickerPreview.OnColorSelectedListener, TextToSpeech.OnInitListener {

    private Camera mCamera;
    private CameraAsyncTask mCameraAsyncTask;
    private CameraColorPickerPreview mCameraPreview;
    private FrameLayout mPreviewContainer;
    private View mPointerRing;
    private TextView mText;

    private TextToSpeech TextoAudio;
    private boolean FlashLigado;
    private String temp = "";
    private boolean AudioLigado;
    private boolean DA;
    private SistemaCoresRAL RAL = new SistemaCoresRAL();

    ImageButton FlashB;
    ImageButton Audio;
    private static final int REQUEST_CAMERA = 100;
    private int cor;
    private String CorName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detectar_cor);
        mPreviewContainer = findViewById(R.id.camera_container);
        mPointerRing = findViewById(R.id.pointer_ring);
        mText = findViewById(R.id.Texto);
        FlashB = (ImageButton) findViewById(R.id.Flash);
        Audio = (ImageButton) findViewById(R.id.Audio);
        FloatingActionButton Miu = findViewById(R.id.Floating);
        final SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent A = new Intent(ActivityDetectarCor.this, sugestao.class);
                User(preferences,cor);
                startActivity(A);
            }
        });

        DA = true;
        Audio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AtivarDezativarBotao();
            }
        });
        FlashB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Flash();
            }
        });
        Miu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent A = new Intent(ActivityDetectarCor.this, MainActivity.class);
                startActivity(A);
                finish();
            }
        });
    }
    private void User(SharedPreferences preferences, int corA) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("RGB","("+Color.red(corA)+", "+Color.green(corA)+", "+Color.blue(corA)+")");
        editor.putString("NomeCor",CorName);
        editor.commit();
    }
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            TextoAudio.setLanguage(Locale.getDefault());
        }
    }

    private void Fala(String cor) {
        TextoAudio.speak(cor, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void AtivarDezativarBotao(){
        if (AudioLigado == true){
            AudioLigado = false;
            Audio.setBackgroundResource(R.drawable.ic_volume_off);
        }
        else {
            AudioLigado = true;
            Audio.setBackgroundResource(R.drawable.ic_volume_on);
        }
    }
    @Override
    public void onColorSelected(int color) {
        mPointerRing.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        cor = color;
        RAL.ProcurarCor(color);
        // Nome da Cor
        CorName = RAL.getNomeDaCor();
        mText.setText(CorName);

        //Sistema de Audio
        if (AudioLigado == true){
            if(RAL.getNomeDaCor() != temp){
                Fala(RAL.getNomeDaCor());
            }
            temp = RAL.getNomeDaCor();
        }
    }

    private static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public void onResume() {
        super.onResume();

        FlashLigado = false;
        Audio.setBackgroundResource(R.drawable.ic_volume_off);
        TextoAudio = new TextToSpeech(this,this);
        SharedPreferences M = getSharedPreferences("User", MODE_PRIVATE);
        if((M.contains("Cegueira"))&&(DA)){
            AudioLigado = true;
            Audio.setBackgroundResource(R.drawable.ic_volume_on);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ActivityDetectarCor.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityDetectarCor.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            } else {
                mCameraAsyncTask = new CameraAsyncTask();
                mCameraAsyncTask.execute();
            }
        } else {
            mCameraAsyncTask = new CameraAsyncTask();
            mCameraAsyncTask.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        FlashB.setBackgroundResource(R.drawable.ic_flashoff);
        if(!AudioLigado){
            DA = false;
        }
        if (mCameraAsyncTask != null) {
            mCameraAsyncTask.cancel(true);
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
        if (mCameraPreview != null) {
            mPreviewContainer.removeView(mCameraPreview);
        }
        if(TextoAudio !=null){
            TextoAudio.stop();
            TextoAudio.shutdown();
        }
    }

    protected void Flash() {
        ImageButton FlashB = (ImageButton) findViewById(R.id.Flash);
        if (mCamera != null) {
            final Camera.Parameters parameters = mCamera.getParameters();
            final String flashParameter;
            if(FlashLigado){
                flashParameter = Camera.Parameters.FLASH_MODE_OFF;
                FlashB.setBackgroundResource(R.drawable.ic_flashoff);
            }
            else{
                flashParameter = Camera.Parameters.FLASH_MODE_TORCH;
                FlashB.setBackgroundResource(R.drawable.ic_flash);
            }
            parameters.setFlashMode(flashParameter);

            // Parando o Preview
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();

            // Mudando Paranmetros da Camera
            mCamera.setParameters(parameters);

            // Reiniciando Preview
            mCamera.setPreviewCallback(mCameraPreview);
            mCamera.startPreview();

            FlashLigado = !FlashLigado;
        }
    }
    private class CameraAsyncTask extends AsyncTask<Void, Void, Camera> {

        protected FrameLayout.LayoutParams mPreviewParams;

        @Override
        protected Camera doInBackground(Void... params) {
            Camera camera = getCameraInstance();
            if (camera != null) {
                Camera.Parameters cameraParameters = camera.getParameters();
                @SuppressLint("WrongThread") Camera.Size bestSize = Cameras.getBestPreviewSize(cameraParameters.getSupportedPreviewSizes(), mPreviewContainer.getWidth(), mPreviewContainer.getHeight(), true);
                cameraParameters.setPreviewSize(bestSize.width, bestSize.height);
                camera.setParameters(cameraParameters);
                Cameras.setCameraDisplayOrientation(ActivityDetectarCor.this, camera);
                @SuppressLint("WrongThread") int[] adaptedDimension = Cameras.getProportionalDimension(bestSize, mPreviewContainer.getWidth(), mPreviewContainer.getHeight(), true);
                mPreviewParams = new FrameLayout.LayoutParams(adaptedDimension[0], adaptedDimension[1]);
                mPreviewParams.gravity = Gravity.CENTER;
            }
            return camera;
        }

        @Override
        protected void onPostExecute(Camera camera) {
            super.onPostExecute(camera);
            if (!isCancelled()) {
                mCamera = camera;
                if (mCamera != null) {
                    mCameraPreview = new CameraColorPickerPreview(ActivityDetectarCor.this, mCamera);
                    mCameraPreview.setOnColorSelectedListener(ActivityDetectarCor.this);
                    mPreviewContainer.addView(mCameraPreview, 0, mPreviewParams);
                }
            }
        }

        @Override
        protected void onCancelled(Camera camera) {
            super.onCancelled(camera);
            if (camera != null) {
                camera.release();
            }
        }
    }
}
