package com.danielacedo.introduccionadatos_ficheros.Ejercicio2.interfaces;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio2.Alarm;

/**
 * Created by Daniel on 16/11/2016.
 */

public interface AlarmCounterCallBack {
    /**
     * Called every onTick
     * @param currentAlarm current alarm running
     * @param millisUntilFinished milliseconds until this alarm finishes
     */
    void onTick(Alarm currentAlarm, int currentAlarmPosition, int alarmsLeft, long millisUntilFinished, long minutesLeft, long secondsLeft);

    /**
     * Called whenever a alarm from the list finishes
     * @param finishedAlarm Alarm that just finished
     */
    void onAlarmFinish(Alarm finishedAlarm);

    void onAllAlarmsFinish();
}
