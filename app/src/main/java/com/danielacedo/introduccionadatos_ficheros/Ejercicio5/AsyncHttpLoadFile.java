package com.danielacedo.introduccionadatos_ficheros.Ejercicio5;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Daniel on 14/11/16.
 */

public class AsyncHttpLoadFile {
    private AsyncHttpClient client;
    String path;

    public AsyncHttpLoadFile(String path){
        client = new AsyncHttpClient();
        this.path = path;
    }

    public String[] loadStringArray(Context context) throws FileNotFoundException{

        List<String> urls = new ArrayList<String>();

        client.get(path, new FileAsyncHttpResponseHandler(context) {
            @Override
            public void onStart() {
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response){
                //TODO Leer las lineas
                
            }
        });

        return (String[])urls.toArray();
    }

}
