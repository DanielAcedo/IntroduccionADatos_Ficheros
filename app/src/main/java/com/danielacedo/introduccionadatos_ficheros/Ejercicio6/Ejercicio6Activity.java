package com.danielacedo.introduccionadatos_ficheros.Ejercicio6;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio3.Ejercicio3Activity;
import com.danielacedo.introduccionadatos_ficheros.R;
import com.danielacedo.introduccionadatos_ficheros.RestClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class Ejercicio6Activity extends AppCompatActivity {

    private String urlApiRatio = "http://api.fixer.io/latest";
    private String archivoDivisas = "divisas";

    private List<Conversion> rateList;
    private Conversion conversionActual;

    private TextView txv_InfoDivisa, txv_Euros, txv_Dolares;
    private EditText edt_Dolares, edt_Euros;
    private RadioButton rbt_EurosADolares, rbt_DolaresAEuros;
    private Button btn_ActualizarRatio, btn_Convertir;
    private Spinner spn_SelectCountry;

    private int ultimaPosicionDivisa = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Conversión divisas");
        setContentView(R.layout.activity_ejercicio6);

        //Coger los Views
        txv_InfoDivisa = (TextView)findViewById(R.id.txv_InfoDivisa);
        txv_Euros = (TextView)findViewById(R.id.txv_Euros);
        txv_Dolares = (TextView)findViewById(R.id.txv_Dolares);
        edt_Euros = (EditText)findViewById(R.id.edt_Euros);
        edt_Dolares = (EditText)findViewById(R.id.edt_Dolares);
        btn_ActualizarRatio = (Button)findViewById(R.id.btn_ActualizarRatio);
        btn_Convertir = (Button)findViewById(R.id.btn_Convertir);
        rbt_DolaresAEuros = (RadioButton)findViewById(R.id.rbt_DolaresAEuros);
        rbt_EurosADolares = (RadioButton)findViewById(R.id.rbt_EurosADolares);
        spn_SelectCountry = (Spinner)findViewById(R.id.spn_SelectCountry);


        //Inicializar valores
        rateList = new ArrayList<Conversion>();
        conversionActual = new Conversion(Conversion.PAIS_DEFECTO, Conversion.RATIO_DEFECTO);
        edt_Dolares.setText("0.00");
        edt_Euros.setText("0.00");
        rbt_EurosADolares.setChecked(true);

        spn_SelectCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                conversionActual = rateList.get(i);
                actualizarConversion();
                btn_Convertir.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Llamar al servidor para coger los datos del ratio de divisas
        actualizarDivisas();


    }

    public void onClick(View v)  {
        switch (v.getId()){
            case R.id.btn_ActualizarRatio:
                actualizarDivisas();
                break;
            case R.id.btn_Convertir:
                if(rbt_DolaresAEuros.isChecked()){
                    parsearDolares();
                }else if(rbt_EurosADolares.isChecked()){
                    parsearEuros();
                }
        }

    }

    public void actualizarConversion(){
        txv_Dolares.setText(conversionActual.getCodPais());
        rbt_DolaresAEuros.setText(conversionActual.getCodPais()+" a EUR");
        rbt_EurosADolares.setText("EUR a "+conversionActual.getCodPais());
        txv_InfoDivisa.setText(getResources().getString(R.string.txv_InfoDivisa_text_correcto)+String.valueOf(conversionActual.getRatio())+" "+conversionActual.getCodPais()+"/EUR");
    }

    private void parsearEuros(){
        try{
            double euros = Double.parseDouble(edt_Euros.getText().toString());
            edt_Euros.setText(String.format(Locale.US,"%.2f", euros)); //Añadimos los decimales si lo introducido es entero
            edt_Dolares.setText(String.format(Locale.US, "%.2f", conversionActual.ConvertirADivisa(euros)));
        }catch(NumberFormatException e){
            mostrar(getResources().getString(R.string.parseEurosError));
        }
    }

    private void parsearDolares(){
        try{
            double dolares = Double.parseDouble(edt_Dolares.getText().toString());
            edt_Dolares.setText(String.format(Locale.US,"%.2f", dolares)); //Añadimos los decimales si lo introducido es entero
            edt_Euros.setText(String.format(Locale.US, "%.2f", conversionActual.ConvertirAEuros(dolares))); //Se convierte con 2 decimales
        }catch(NumberFormatException e){
            mostrar(getResources().getString(R.string.parseDolaresError));
        }
    }

    /**
     * Makes a GET Call to the api to obtain rates data
     */
    private void actualizarDivisas(){
        btn_ActualizarRatio.setText(R.string.btn_InfoDivisa_text_conectando);

        RestClient.get(urlApiRatio, new JsonHttpResponseHandler(){  //Now using AsyncHTTPClient
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if(spn_SelectCountry!=null){
                        ultimaPosicionDivisa = spn_SelectCountry.getSelectedItemPosition();
                    }

                    tratarJSON(response);

                    if(spn_SelectCountry!=null){
                       spn_SelectCountry.setSelection(ultimaPosicionDivisa);
                    }

                    txv_InfoDivisa.setText(getResources().getString(R.string.txv_InfoDivisa_text_correcto)+String.valueOf(conversionActual.getRatio())+" "+conversionActual.getCodPais()+"/EUR");
                    btn_ActualizarRatio.setText(R.string.btn_ActualizarRatio_text);

                } catch (JSONException e) {
                    Toast.makeText(Ejercicio6Activity.this, "Error JSON: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(Ejercicio6Activity.this, "Error al conectar con el servidor. Leyendo fichero local...", Toast.LENGTH_SHORT).show();

                try{
                    FileInputStream in = openFileInput(archivoDivisas);
                    String json = "";
                    int data;

                    while((data=in.read())!= -1){
                        json+=(char)data;
                    }

                    in.close();
                    tratarJSON(new JSONObject(json));
                }catch(IOException e){
                    Toast.makeText(Ejercicio6Activity.this, "No hay datos anteriores. Intenta conectar al menos una vez con el servidor", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(Ejercicio6Activity.this, "Error al leer el fichero local.", Toast.LENGTH_SHORT).show();
                }
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

        //new Ejercicio6Activity.GetHttpResponse().execute(urlApiRatio);
    }

    /**
     * Read JSON received from the API and populate the spinner with the data
     * @param jsonImportado JSON received
     * @throws JSONException
     */
    private void tratarJSON(JSONObject jsonImportado) throws JSONException{

            JSONObject json = jsonImportado;
            JSONObject ratios = json.getJSONObject("rates");

            Iterator<String> keys = ratios.keys();
            rateList = new ArrayList<Conversion>();

            while (keys.hasNext()){
                String divisa = keys.next();
                Double ratio = ratios.getDouble(divisa);
                rateList.add(new Conversion(divisa, ratio));
            }

            //Popular el Spinner
            List<String> paises = new ArrayList<String>();

            for(Conversion c : rateList){
                paises.add(c.getCodPais());
            }

            ArrayAdapter<String> paisesAdapter = new ArrayAdapter<String>(Ejercicio6Activity.this, android.R.layout.simple_spinner_item, paises);
            paisesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spn_SelectCountry.setAdapter(paisesAdapter);
            edt_Dolares.setText("0.00");
            edt_Euros.setText("0.00");



    }


    public void mostrar(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
