package com.danielacedo.introduccionadatos_ficheros;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.squareup.picasso.Picasso;

public class Ejercicio5MainActivity extends AppCompatActivity {

    EditText edt_FilePath;
    Button btn_Download, btn_GalleryDown, btn_GalleryUp;
    ImageView imv_image;

    private int currentImage;
    private int gallerySize;

    private String[] gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio5_main);

        gallery = new String[gallerySize];

        edt_FilePath = (EditText)findViewById(R.id.edt_FilePath);
        btn_Download = (Button)findViewById(R.id.btn_Download);
        btn_GalleryDown = (Button)findViewById(R.id.btn_galleryDown);
        btn_GalleryUp = (Button)findViewById(R.id.btn_galleryUp);
        imv_image = (ImageView)findViewById(R.id.imv_image);
    }

    private void galleryDown(){
        currentImage = (currentImage-1)%gallerySize;
    }

    private void setBtn_GalleryUp(){
        currentImage = (currentImage+1)%gallerySize;
    }

    private void setImage(){
        Picasso.with(Ejercicio5MainActivity.this).load(gallery[currentImage]).into(imv_image);
    }


}
