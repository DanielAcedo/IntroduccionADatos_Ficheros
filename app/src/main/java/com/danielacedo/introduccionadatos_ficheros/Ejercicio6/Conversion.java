package com.danielacedo.introduccionadatos_ficheros.Ejercicio6;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Daniel on 27/09/16.
 */

public class Conversion {
    static public final double RATIO_DEFECTO = 1.12;
    static public final String PAIS_DEFECTO = "USD";

    private String codDivisa = "";
    private double ratioActual = 0.0;

    public Conversion(){
        codDivisa = PAIS_DEFECTO;
        ratioActual = RATIO_DEFECTO;
    }

    public Conversion(String codDivisa, double ratioActual){
        this.codDivisa = codDivisa;
        this.ratioActual = ratioActual;
    }

    public double ConvertirAEuros(double otraDivisa){
        return otraDivisa/ratioActual;
    }

    public double ConvertirADivisa(double euros){
        return euros*ratioActual;
    }

    public double getRatio(){
        return ratioActual;
    }

    public void setRatio(double ratio){
        ratioActual = ratio;
    }

    public String getCodPais(){
        return codDivisa;
    }

    public void setCodPais(String codPais){
        codDivisa = codPais;
    }

}
