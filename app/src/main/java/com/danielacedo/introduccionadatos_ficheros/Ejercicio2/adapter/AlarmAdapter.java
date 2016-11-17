package com.danielacedo.introduccionadatos_ficheros.Ejercicio2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio2.Alarm;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio2.AlarmFile;
import com.danielacedo.introduccionadatos_ficheros.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 16/11/2016.
 */

public class AlarmAdapter extends ArrayAdapter<Alarm> {

    public AlarmAdapter(Context context){
        super(context, R.layout.alarm_layout, AlarmFile.getAlarms());
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AlarmHolder holder;
        View v = convertView;

        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.alarm_layout, null);
            holder = new AlarmHolder();

            holder.txv_name = (TextView)v.findViewById(R.id.txv_alarmList_name);
            holder.txv_duration = (TextView)v.findViewById(R.id.txv_alarmList_duration);
            holder.btn_remove = (ImageButton)v.findViewById(R.id.btn_alarmList_remove);

            holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAlarm(position);
                }
            });

            v.setTag(holder);
        }else{
            holder = (AlarmHolder)v.getTag();
        }

        holder.txv_name.setText("Alarma "+(position+1));
        holder.txv_duration.setText(getItem(position).getMinutes()+" minutos");

        return v;
    }

    public void removeAlarm(int position){
        if(position < getCount()){
            AlarmFile.removeAlarm(position);
            remove(getItem(position));
            notifyDataSetChanged();
        }
    }

    public void addAlarm(Alarm alarm){
        AlarmFile.saveAlarm(alarm);
        add(alarm);
        notifyDataSetChanged();
    }

    public List<Alarm> getAll(){
        List<Alarm> alarms = new ArrayList<Alarm>();

        for(int i = 0; i < getCount(); i++){
            alarms.add(getItem(i));
        }

        return alarms;
    }

    public static class AlarmHolder{
        TextView txv_name, txv_duration;
        ImageButton btn_remove;
    }
}
