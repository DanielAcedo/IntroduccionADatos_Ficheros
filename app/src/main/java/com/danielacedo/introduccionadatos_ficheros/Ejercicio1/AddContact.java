package com.danielacedo.introduccionadatos_ficheros.Ejercicio1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.danielacedo.introduccionadatos_ficheros.R;

import java.util.List;
import java.util.UUID;

public class AddContact extends AppCompatActivity {

    private EditText edt_ContactName, edt_ContactTelephone, edt_ContactEmail;
    private Button btn_Add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        edt_ContactName = (EditText)findViewById(R.id.edt_ContactName);
        edt_ContactTelephone = (EditText)findViewById(R.id.edt_ContactTlf);
        edt_ContactEmail = (EditText)findViewById(R.id.edt_ContactEmail);
        btn_Add = (Button)findViewById(R.id.btn_Add);

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_ContactName.getText().toString();
                String telephone = edt_ContactTelephone.getText().toString();
                String email = edt_ContactEmail.getText().toString();

                List<Contact> contacts = ContactFile.getContacts(AddContact.this);
                int newId;
                if(contacts.size()==0){
                    newId=0;
                }else{
                    newId = contacts.get(contacts.size()-1).getId()+1;
                }
                Contact contact = new Contact(newId, name, telephone, email);
                ContactFile.saveContact(AddContact.this, contact);
                finish();
            }
        });
    }
}
