package com.danielacedo.introduccionadatos_ficheros.Ejercicio2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.EnvironmentCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 16/11/2016.
 */

public class AlarmFile {
    private static final String FILE_NAME = "alarmas.txt";


    public static boolean createFileExternal(){
        boolean ok = true;

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    ok = false;
                }
            }
        }else{
            ok = false;
        }

        return ok;
    }

    public static File getFile(){
        File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);

        return file;
    }

    public static void resetFile(){
        if(createFileExternal()){
            File file = getFile();
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean saveAlarm(Alarm alarm){
        boolean ok = true;

        if(createFileExternal()){
            File file = getFile();
            try {
                FileWriter writer = new FileWriter(file, true);
                writer.append(String.valueOf(alarm.getMinutes())+";"+alarm.getFinishMessage()+"\n");

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                ok = false;
            }
        }else{
            ok = false;
        }


        return ok;
    }

    public static boolean removeAlarm(int position){
        boolean ok = true;

        if(createFileExternal()){
            List<Alarm> alarms = getAlarms();
            alarms.remove(position);

            resetFile();
            for(Alarm alarm : alarms){
                saveAlarm(alarm);
            }
        }else{
            ok = false;
        }

        return ok;
    }

    public static List<Alarm> getAlarms(){
        List<Alarm> alarms = new ArrayList<Alarm>();

        if(createFileExternal()){
            File file = getFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while((line = reader.readLine()) != null){
                    int minutes = Integer.parseInt(line.substring(0, line.indexOf(";")));
                    String message = line.substring(line.indexOf(";")+1);

                    alarms.add(new Alarm(minutes, message));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return alarms;
    }
}
