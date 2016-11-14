package com.danielacedo.introduccionadatos_ficheros;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
            case R.id.btn_Ej5:
                intent = new Intent(MainActivity.this, Ejercicio5MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
