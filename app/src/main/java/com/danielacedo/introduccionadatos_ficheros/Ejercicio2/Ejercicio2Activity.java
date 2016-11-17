package com.danielacedo.introduccionadatos_ficheros.Ejercicio2;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio2.adapter.AlarmAdapter;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio2.interfaces.AlarmCounterCallBack;
import com.danielacedo.introduccionadatos_ficheros.R;

import java.util.List;

public class Ejercicio2Activity extends AppCompatActivity {

    public static final int REQUEST_ADD_ALARM = 1;

    private AlarmCounter alarmCounter;
    private List<Alarm> alarms;
    private int notificationId = 0;

    private TextView txv_currentAlarmIndex, txv_currentAlarmTime;
    private Button btn_AddAlarm, btn_Init;
    private Switch sw_StopAlarm;
    private ListView lv_alarms;
    private AlarmAdapter adapter;

    private boolean countOnBackGround = false; //Do the counter keep running after exiting the activity?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio2);

        txv_currentAlarmIndex = (TextView) findViewById(R.id.txv_currentAlarmIndex);
        txv_currentAlarmTime = (TextView) findViewById(R.id.txv_currentAlarmTime);
        btn_AddAlarm = (Button)findViewById(R.id.btn_AddAlarm);
        btn_Init = (Button)findViewById(R.id.btn_Init);
        sw_StopAlarm = (Switch)findViewById(R.id.sw_StopAlarm);
        sw_StopAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countOnBackGround = isChecked;
            }
        });
        lv_alarms = (ListView)findViewById(R.id.lv_alarms);

        adapter = new AlarmAdapter(this);
        lv_alarms.setAdapter(adapter);


        btn_AddAlarm.setOnClickListener(new View.OnClickListener() { //Open activity for creating new alarm
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ejercicio2Activity.this, AddAlarm.class);
                startActivityForResult(intent, REQUEST_ADD_ALARM);
            }
        });


        btn_Init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimer();
            }
        });

    }

    /**
     * Create and starts an AlarmCounter, if there is any already running, it stops it first.
     */
    private void initTimer(){
        if(adapter.getCount()!=0){
            if(alarmCounter!=null)
                alarmCounter.reset();

            alarmCounter = new AlarmCounter(adapter.getAll(), new AlarmCounterCallBack() {
                @Override
                public void onTick(Alarm currentAlarm, int currentAlarmPosition, int alarmsLeft, long millisUntilFinished, long minutesLeft, long secondsLeft) {
                    txv_currentAlarmIndex.setText("Alarma "+(currentAlarmPosition+1)+", faltan "+alarmsLeft+" alarmas");
                    txv_currentAlarmTime.setText("Quedan "+String.valueOf(minutesLeft)+" minutos, "+String.valueOf(secondsLeft)+" segundos para la siguiente alarma");
                }

                @Override
                public void onAlarmFinish(Alarm finishedAlarm) {
                    Toast.makeText(Ejercicio2Activity.this, finishedAlarm.getFinishMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAllAlarmsFinish() {
                    launchNotification("Terminado", "Todas las alarmas terminadas");

                }
            });

            alarmCounter.start();
        }
    }


    /**
     * Adds a new alawm to the adapter
     * @param minutes Minutes that the alarm will be running for
     * @param message Message displayed once finished
     */
    private void saveAlarm(int minutes, String message){
        if(adapter.getCount() < 5){
            adapter.addAlarm(new Alarm(minutes, message));
        }else{
            Toast.makeText(Ejercicio2Activity.this, "Ya hay 5 alarmas. No se pueden añadir mas", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Launch push notification
     * @param title Title of the notification
     * @param content Content of the notification
     */
    private void launchNotification(String title, String content){
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(Ejercicio2Activity.this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setContentText(content).setPriority(Notification.PRIORITY_HIGH).setDefaults(Notification.DEFAULT_VIBRATE);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId++, nBuilder.build());
    }

    @Override
    public void onBackPressed() {
        if(!countOnBackGround){
            if(alarmCounter != null){
                alarmCounter.reset();
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {

            if(!countOnBackGround){
                if(alarmCounter != null){
                    alarmCounter.reset();
                }
            }

        super.onStop();
    }

    @Override
    protected void onResume() {
            if(!countOnBackGround){
                if(alarmCounter!=null){
                    alarmCounter.start();
                }
            }

        super.onResume();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ADD_ALARM){ //Added an alarm
            if (resultCode == AddAlarm.RESULT_ALARM_ADDED){
                int minutes = data.getIntExtra(AddAlarm.NEW_ALARM_MINUTES, 1);
                String mensaje = data.getStringExtra(AddAlarm.NEW_ALARM_MESSAGE);

                saveAlarm(minutes, mensaje);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
