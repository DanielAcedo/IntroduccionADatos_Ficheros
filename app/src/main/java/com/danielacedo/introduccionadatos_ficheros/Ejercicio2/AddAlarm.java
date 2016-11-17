package com.danielacedo.introduccionadatos_ficheros.Ejercicio2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.Contact;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.ContactFile;
import com.danielacedo.introduccionadatos_ficheros.R;

import java.util.List;

public class AddAlarm extends AppCompatActivity {

    public static final int RESULT_ALARM_ADDED = 2;
    public static final String NEW_ALARM_MINUTES = "minutes";
    public static final String NEW_ALARM_MESSAGE = "message";


    private EditText edt_AlarmMinute, edt_AlarmMessage;
    private Button btn_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        edt_AlarmMinute = (EditText)findViewById(R.id.edt_AlarmMinutes);
        edt_AlarmMessage = (EditText)findViewById(R.id.edt_AlarmMessage);
        btn_Add = (Button)findViewById(R.id.btn_Add);

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_AlarmMinute.getText().toString().isEmpty()){
                    Toast.makeText(AddAlarm.this, "Los minutos no pueden estar vacios", Toast.LENGTH_SHORT).show();
                }else{
                    int minutes = Integer.parseInt(edt_AlarmMinute.getText().toString());
                    String message = edt_AlarmMessage.getText().toString();

                    if(checkFields(minutes, message)){

                        //Return data to main activity
                        Intent intent = new Intent();
                        intent.putExtra(NEW_ALARM_MINUTES, minutes);
                        intent.putExtra(NEW_ALARM_MESSAGE, message);
                        setResult(RESULT_ALARM_ADDED, intent);
                        finish();
                    }
                }
            }
        });
    }

    private boolean checkFields(int minutes, String message){
        boolean result = true;

        if(minutes == 0){
            edt_AlarmMinute.setError("El nombre no puede estar vacío");
            result = false;
        }

        if(message.isEmpty()){
            edt_AlarmMessage.setError("El mensaje no puede estar vacío");
            result = false;
        }

        return result;
    }
}
