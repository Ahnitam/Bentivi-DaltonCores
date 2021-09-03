package com.android.bentivi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

public class Dinheiro extends AppCompatActivity implements CameraColorPickerPreview.OnColorSelectedListener, TextToSpeech.OnInitListener{
    private Camera mCamera;
    private Dinheiro.CameraAsyncTask mCameraAsyncTask;
    private CameraColorPickerPreview mCameraPreview;
    private FrameLayout mPreviewContainer;

    private View mPointerRing;
    private TextView mText;

    private TextToSpeech TextoAudio;
    private boolean FlashLigado;
    private String temp = "";
    private boolean AudioLigado;
    private SistemaCoresRAL RAL = new SistemaCoresRAL();

    private static final int REQUEST_CAMERA = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dinheiro);
        mPreviewContainer = findViewById(R.id.camera_container);
        mPointerRing = findViewById(R.id.pointer_ring);
        mText = findViewById(R.id.Texto);
        AudioLigado = false;

        ImageButton Flash = (ImageButton) findViewById(R.id.Flash);
        ImageButton Audio = (ImageButton) findViewById(R.id.Audio);

        Audio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AtivarDezativarBotao();
            }
        });
        Flash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Flash();
            }
        });
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
        }
        else {
            AudioLigado = true;
        }
    }
    @Override
    public void onColorSelected(int color) {
        mPointerRing.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        RAL.ProcurarCor(color);
        // Nome da Cor
        mText.setText("20 Reais");
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

        TextoAudio = new TextToSpeech(this,this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Dinheiro.this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Dinheiro.this,
                        new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
            } else {
                mCameraAsyncTask = new Dinheiro.CameraAsyncTask();
                mCameraAsyncTask.execute();
            }
        } else {
            mCameraAsyncTask = new Dinheiro.CameraAsyncTask();
            mCameraAsyncTask.execute();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
        if (mCamera != null) {
            final Camera.Parameters parameters = mCamera.getParameters();
            final String flashParameter;
            if(FlashLigado){
                flashParameter = Camera.Parameters.FLASH_MODE_OFF;
            }
            else{
                flashParameter = Camera.Parameters.FLASH_MODE_TORCH;
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
                Cameras.setCameraDisplayOrientation(Dinheiro.this, camera);
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
                    mCameraPreview = new CameraColorPickerPreview(Dinheiro.this, mCamera);
                    mCameraPreview.setOnColorSelectedListener(Dinheiro.this);
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
