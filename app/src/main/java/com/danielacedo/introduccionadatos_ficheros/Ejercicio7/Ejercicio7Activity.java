package com.danielacedo.introduccionadatos_ficheros.Ejercicio7;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.danielacedo.introduccionadatos_ficheros.R;
import com.danielacedo.introduccionadatos_ficheros.RestClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Permission;
import java.security.Permissions;

import cz.msebera.android.httpclient.Header;

public class Ejercicio7Activity extends AppCompatActivity implements View.OnClickListener {

    public final static String WEB = "https://192.168.3.61/datos/upload.php";

    private EditText texto;
    Button boton;
    TextView informacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio7);

        texto = (EditText)findViewById(R.id.texto);
        boton = (Button)findViewById(R.id.button);
        informacion = (TextView)findViewById(R.id.textView2);
    }

    @Override
    public void onClick(View v) {
        subida();
    }

    private void subida() {
        //Permission check for Android6 onwards
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        String fichero = texto.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(Ejercicio7Activity.this);
        File myFile;
        Boolean existe = true;
        myFile = new File(Environment.getExternalStorageDirectory(), fichero);
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();

        try {
            //If the file doesn't exist, create one
            if(!myFile.exists())
                myFile.createNewFile();
            params.put("fileToUpload", myFile);
        } catch (IOException e) {
            existe = false;
            informacion.setText("Error en el fichero: " + e.getMessage());
            //Toast.makeText(this, "Error en el fichero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        if (existe)
            RestClient.post(WEB, params, new TextHttpResponseHandler() { //Post call
                @Override
                public void onStart() {
                    // called before request is started
                    progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progreso.setMessage("Conectando . . .");
                    //progreso.setCancelable(false);
                    progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            RestClient.cancelRequests(getApplicationContext(), true);
                        }
                    });
                    progreso.show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String response) {
                    // called when response HTTP status is "200 OK"
                    progreso.dismiss();
                    informacion.setText(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    informacion.setText(response);
                    progreso.dismiss();
                }
            });
    }
}
