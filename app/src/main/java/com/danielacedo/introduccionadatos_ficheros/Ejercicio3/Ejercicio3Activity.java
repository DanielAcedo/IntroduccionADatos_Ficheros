package com.danielacedo.introduccionadatos_ficheros.Ejercicio3;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio4.Ejercicio4Activity;
import com.danielacedo.introduccionadatos_ficheros.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Ejercicio3Activity extends AppCompatActivity {

    private final int MIN_CYCLE_DAYS = 21;
    private final int MAX_CYCLE_DAYS = 35;
    private final int DEFAULT_CYCLE_DAY = 28;

    private final String FILENAME = "diasfertiles.txt";

    private NumberPicker np_cycle;
    private DatePicker dp_firstBlood;
    private Button btn_Calculate;

    private DateTime calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio3);

        calendar = new DateTime(DateTimeZone.getDefault());

        np_cycle = (NumberPicker) findViewById(R.id.np_cycle);
        np_cycle.setMinValue(MIN_CYCLE_DAYS);
        np_cycle.setMaxValue(MAX_CYCLE_DAYS);
        np_cycle.setValue(DEFAULT_CYCLE_DAY);

        btn_Calculate = (Button) findViewById(R.id.btn_Calculate);
        btn_Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateFertileDays(calendar, np_cycle.getValue());
            }
        });

        dp_firstBlood = (DatePicker) findViewById(R.id.dp_firstBlood);
        dp_firstBlood.init(calendar.getYear(), calendar.getMonthOfYear()-1, calendar.getDayOfMonth(), new DatePicker.OnDateChangedListener() { //Calendar months starts with 0 and DateTime with 1
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar = calendar.withYear(year).withMonthOfYear(monthOfYear+1).withDayOfMonth(dayOfMonth);
            }
        });
    }


    private void calculateFertileDays(DateTime cal, int cycleDays) {
        final DateTime ovulationDay = cal.plusDays(cycleDays).minusDays(14);
        DateTime today = new DateTime();

        boolean isFertile = (today.isBefore(ovulationDay.plusDays(2)) && today.isAfter(ovulationDay.minusDays(3))); //If today is between the fertileDays
        String dialogMessage = "Ovulación: " + ovulationDay.toString("d/M") + "\nDías fertiles: De " + ovulationDay.minusDays(2).toString("d/M") + " a " + ovulationDay.plusDays(1).toString("d/M")+"\n";

        if (isFertile) {
            dialogMessage+="\n¡Hoy te encuentras en un día fertil!";
        }else{
            dialogMessage+="\nHoy no te encuentras en un día fertil";
        }

        AlertDialog dialog = new AlertDialog.Builder(Ejercicio3Activity.this).create();
        dialog.setTitle("Fertilidad");
        dialog.setMessage(dialogMessage);
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                saveFertileDays(ovulationDay.minusDays(2), ovulationDay.plusDays(1));
            }
        });

        dialog.show();

    }

    private void saveFertileDays(DateTime firstDay, DateTime lastDay){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(Environment.getExternalStorageDirectory(), FILENAME);

            try {

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
                    writer.write(firstDay.toString("d-M-y")+";"+lastDay.toString("d-M-y"));
                    writer.close();
                    Toast.makeText(Ejercicio3Activity.this, "Dias fertiles guardadados en "+ file.getPath(), Toast.LENGTH_SHORT).show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
