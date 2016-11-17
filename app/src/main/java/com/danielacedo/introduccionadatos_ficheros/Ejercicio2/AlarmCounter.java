package com.danielacedo.introduccionadatos_ficheros.Ejercicio2;

import android.os.CountDownTimer;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio2.interfaces.AlarmCounterCallBack;

import java.util.List;

/**
 * Created by Daniel on 16/11/2016.
 */

public class AlarmCounter {

    private List<Alarm> alarms;
    private int alarmsLength;
    private int currentAlarm;
    private boolean started;

    CountDownTimer timer;
    AlarmCounterCallBack callBack;

    public AlarmCounter(List<Alarm> alarms, AlarmCounterCallBack callBack){
        this.alarms = alarms;
        this.alarmsLength = alarms.size();
        this.currentAlarm = 0;
        this.callBack = callBack;
        this.started = false;
        newTimer();
    }

    public void start(){ //Only called one time until we reset
        if(!started){
            timer.start();
            started = true;
        }
    }

    public void reset(){
        currentAlarm = 0;
        started = false;
        timer.cancel();
        newTimer();
    }

    private void nextTimer(){
        currentAlarm++;
        newTimer();
        timer.start();
    }

    private void newTimer(){
        timer = new CountDownTimer((alarms.get(currentAlarm).getMinutes() * 60000)+100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                AlarmCounter.this.onTick(millisUntilFinished);

            }

            @Override
            public void onFinish() {
                AlarmCounter.this.onFinish();
            }
        };
    }

    public void onTick(long millisUntilFinished) {
        long minutes = (millisUntilFinished / 1000) / 60;
        long seconds = (millisUntilFinished / 1000) % 60;
        callBack.onTick(alarms.get(currentAlarm), currentAlarm, alarmsLength-currentAlarm, millisUntilFinished, minutes, seconds);
    }


    public void onFinish() {
        callBack.onTick(alarms.get(currentAlarm), currentAlarm, (alarmsLength-currentAlarm)-1 , 0, 0, 0); //Force to notify that reached to 0, we substract one to alarmsLeft as we just finished one
        callBack.onAlarmFinish(alarms.get(currentAlarm));

        if(currentAlarm<alarmsLength-1){
            nextTimer();
        }else{
            callBack.onAllAlarmsFinish();
        }
    }
}
