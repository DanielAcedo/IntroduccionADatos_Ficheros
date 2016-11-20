package com.danielacedo.introduccionadatos_ficheros.Ejercicio7;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.danielacedo.introduccionadatos_ficheros.R;
import com.danielacedo.introduccionadatos_ficheros.RestClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class Ejercicio7Activity extends AppCompatActivity{

    public final static String WEB = "http://192.168.1.132/datos/upload.php"; //PHP file that handles the uploading

    public static final int REQUEST_OPENFILE = 1;

    private EditText txv_FileName;
    Button btn_Upload, btn_OpenFile;
    TextView txv_Information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio7);

        txv_FileName = (EditText)findViewById(R.id.txv_FileName);

        btn_Upload = (Button)findViewById(R.id.btn_Upload);
        btn_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subida();
            }
        });

        btn_OpenFile = (Button)findViewById(R.id.btn_OpenFile);
        btn_OpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileManager();
            }
        });

        txv_Information = (TextView)findViewById(R.id.txv_info);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private void subida() {

        if(isNetworkAvailable()){
            //Permission check for Android6 onwards
            int permissionCheck = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }

            String fichero = txv_FileName.getText().toString();
            final ProgressDialog progreso = new ProgressDialog(Ejercicio7Activity.this);
            File myFile;
            Boolean existe = true;
            //myFile = new File(Environment.getExternalStorageDirectory(), fichero); //External
            myFile = new File(fichero);
            RequestParams params = new RequestParams();
            params.put("secretKey","123");

            try {
                //If the file doesn't exist, create one
                if(!myFile.exists())
                    myFile.createNewFile();
                params.put("fileToUpload", myFile);
            } catch (IOException e) {
                existe = false;
                txv_Information.setText("Error en el fichero: " + e.getMessage());
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
                        txv_Information.setText(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        txv_Information.setText(response);
                        progreso.dismiss();
                    }
                });
        }else{
            txv_Information.setText("No hay conexión");
        }

    }

    private void openFileManager(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, REQUEST_OPENFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_OPENFILE){
            if(resultCode == RESULT_OK){
                String fileName;

                //If it's a media file we transform it to local path
                if(data.getData().getPath().startsWith("/external")){
                        fileName = getRealPathFromURI(data.getData());
                }else{
                    fileName = data.getData().getPath();
                }

                txv_FileName.setText(fileName);
            }else{
                Toast.makeText(Ejercicio7Activity.this, "No se pudo seleccionar el archivo", Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx); }
}
