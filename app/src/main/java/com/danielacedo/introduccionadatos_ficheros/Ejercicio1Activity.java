package com.danielacedo.introduccionadatos_ficheros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.AddContact;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.adapter.ContactAdapter;

public class Ejercicio1Activity extends AppCompatActivity {

    Button btn_AddContact;
    ListView lv_Contacts;
    ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio1);

        btn_AddContact = (Button)findViewById(R.id.btn_AddContact);
        btn_AddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ejercicio1Activity.this, AddContact.class);
                startActivity(intent);
            }
        });

        lv_Contacts = (ListView)findViewById(R.id.lv_contacts);
        adapter = new ContactAdapter(Ejercicio1Activity.this);
        lv_Contacts.setAdapter(adapter);
    }
}
