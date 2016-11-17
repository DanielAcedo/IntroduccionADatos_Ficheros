package com.danielacedo.introduccionadatos_ficheros.Ejercicio2;

/**
 * Created by Daniel on 16/11/2016.
 */

public class Alarm {
    private int minutes;
    private String finishMessage;

    public Alarm(int minutes, String finishMessage){
        this.minutes = minutes;
        this.finishMessage = finishMessage;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getFinishMessage() {
        return finishMessage;
    }
}
