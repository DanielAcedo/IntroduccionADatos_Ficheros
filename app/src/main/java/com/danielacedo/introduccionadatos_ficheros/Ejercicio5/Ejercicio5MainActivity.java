package com.danielacedo.introduccionadatos_ficheros.Ejercicio5;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio7.Ejercicio7Activity;
import com.danielacedo.introduccionadatos_ficheros.R;
import com.danielacedo.introduccionadatos_ficheros.RestClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class Ejercicio5MainActivity extends AppCompatActivity {

    EditText edt_FilePath;
    Button btn_Download, btn_GalleryDown, btn_GalleryUp;
    TextView txv_galleryPosition;
    ImageView imv_image;

    private int currentImage;

    private String[] gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio5_main);

        currentImage = 0;
        gallery = new String[1];

        edt_FilePath = (EditText)findViewById(R.id.edt_FilePath);
        btn_Download = (Button)findViewById(R.id.btn_Download);
        btn_GalleryDown = (Button)findViewById(R.id.btn_galleryDown);
        btn_GalleryUp = (Button)findViewById(R.id.btn_galleryUp);
        imv_image = (ImageView)findViewById(R.id.imv_image);
        txv_galleryPosition = (TextView)findViewById(R.id.txv_galleryPosition);

        disableGallery();

        btn_Download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPath()){
                    download();
                }
            }
        });

        btn_GalleryUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtn_GalleryUp();
            }
        });

        btn_GalleryDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBtn_GalleryDown();
            }
        });
    }

    private void setBtn_GalleryDown() {
        if(currentImage == 0){
            currentImage = gallery.length-1;
        }else{
            currentImage = (currentImage-1)%gallery.length;
        }
        setImage();
    }

    private void setBtn_GalleryUp(){
        currentImage = (currentImage+1)%gallery.length;
        setImage();
    }

    private void setImage(){
        try{
            Picasso.with(Ejercicio5MainActivity.this).load(gallery[currentImage]).error(R.drawable.error).placeholder(R.drawable.progressanimation).into(imv_image);
            refreshGalleryPositionText();
        }
        catch(IndexOutOfBoundsException ex){
            Toast.makeText(Ejercicio5MainActivity.this, "No hay imagenes en la galeria", Toast.LENGTH_SHORT).show();
        }
        catch(IllegalArgumentException ex){
            Toast.makeText(Ejercicio5MainActivity.this, "La galeria no está correctamente formateada", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean checkPath(){
        boolean ok = true;

        if(edt_FilePath.getText().toString().isEmpty()){
            Toast.makeText(Ejercicio5MainActivity.this, "Dirección vacía", Toast.LENGTH_SHORT).show();
            ok = false;
        }

        return ok;
    }

    private void download(){


        if(!edt_FilePath.getText().toString().startsWith("http://")){
            String text = "http://"+edt_FilePath.getText().toString();
            edt_FilePath.setText(text);
        }

        final ProgressDialog progress = new ProgressDialog(Ejercicio5MainActivity.this);

        RestClient.get(edt_FilePath.getText().toString(), new FileAsyncHttpResponseHandler(Ejercicio5MainActivity.this) {
            private List<String> urls = new ArrayList<String>();

            @Override
            public void onStart() {
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setMessage("Conectando . . .");
                progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progress.show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progress.dismiss();
                disableGallery();
                Toast.makeText(Ejercicio5MainActivity.this, "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response){
                //Read every line and add each link to the list
                progress.dismiss();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;

                    while((line = reader.readLine()) != null){
                        if(!line.equals("")) { //Ignore blank lines
                            urls.add(line);
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(Ejercicio5MainActivity.this, "Fallo de lectura del archivo", Toast.LENGTH_SHORT);
                }

                if (urls.size() != 0)
                    gallery = new String[urls.size()];

                for(int i = 0; i<urls.size(); i++){
                    gallery[i] = urls.get(i);
                }

                if(checkGalleryFile()){
                    enableGallery();
                    setImage();
                }else{
                    disableGallery();
                    Toast.makeText(Ejercicio5MainActivity.this, "Fichero mal formateado", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void refreshGalleryPositionText(){
        txv_galleryPosition.setText((currentImage+1)+"/"+gallery.length);
    }

    private void enableGallery(){
        btn_GalleryDown.setEnabled(true);
        btn_GalleryUp.setEnabled(true);
        refreshGalleryPositionText();
    }

    private void disableGallery(){
        btn_GalleryDown.setEnabled(false);
        btn_GalleryUp.setEnabled(false);
        txv_galleryPosition.setText("");
    }

    private boolean checkGalleryFile(){
        boolean result = true;

        for (int i = 0; i<gallery.length; i++){
            if(!Patterns.WEB_URL.matcher(gallery[i]).matches()){
                result = false;
                break;
            }
        }

        return result;
    }


}
