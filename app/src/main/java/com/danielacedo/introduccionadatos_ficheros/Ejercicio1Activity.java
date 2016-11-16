package com.danielacedo.introduccionadatos_ficheros;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.AddContact;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.Contact;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.ContactFile;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.adapter.ContactAdapter;

public class Ejercicio1Activity extends AppCompatActivity {

    Button btn_AddContact;
    ListView lv_Contacts;
    ContactAdapter adapter;

    public static final int REQUEST_ADD_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio1);

        btn_AddContact = (Button)findViewById(R.id.btn_AddContact);
        btn_AddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //Open activity for adding contacts
                Intent intent = new Intent(Ejercicio1Activity.this, AddContact.class);
                startActivityForResult(intent, REQUEST_ADD_CONTACT);
            }
        });

        lv_Contacts = (ListView)findViewById(R.id.lv_contacts);
        adapter = new ContactAdapter(Ejercicio1Activity.this);
        lv_Contacts.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_CONTACT){
            if(resultCode == AddContact.RESULT_CONTACT_ADDED){ //If we added a contact, refresh the list
                int id = Integer.parseInt(data.getStringExtra(AddContact.NEW_CONTACT_ID));
                String name = data.getStringExtra(AddContact.NEW_CONTACT_NAME);
                String telephone = data.getStringExtra(AddContact.NEW_CONTACT_TELEPHONE);
                String email = data.getStringExtra(AddContact.NEW_CONTACT_EMAIL);

                Contact newContact = new Contact(id, name, telephone, email);

                adapter.add(newContact);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
