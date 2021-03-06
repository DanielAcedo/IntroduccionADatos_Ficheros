package com.danielacedo.introduccionadatos_ficheros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.Ejercicio1Activity;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio2.Ejercicio2Activity;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio3.Ejercicio3Activity;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio4.Ejercicio4Activity;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio5.Ejercicio5MainActivity;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio6.Ejercicio6Activity;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio7.Ejercicio7Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()){
            case R.id.btn_Ej1:
                intent = new Intent(MainActivity.this, Ejercicio1Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_Ej2:
                intent = new Intent(MainActivity.this, Ejercicio2Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_Ej3:
                intent = new Intent(MainActivity.this, Ejercicio3Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_Ej4:
                intent = new Intent(MainActivity.this, Ejercicio4Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_Ej5:
                intent = new Intent(MainActivity.this, Ejercicio5MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_Ej6:
                intent = new Intent(MainActivity.this, Ejercicio6Activity.class);
                startActivity(intent);
                break;
            case R.id.btn_Ej7:
                intent = new Intent(MainActivity.this, Ejercicio7Activity.class);
                startActivity(intent);
                break;
        }
    }
}
