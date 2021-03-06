package com.danielacedo.introduccionadatos_ficheros.Ejercicio1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.danielacedo.introduccionadatos_ficheros.R;

import java.util.List;
import java.util.regex.Pattern;

public class AddContact extends AppCompatActivity {

    public static final int RESULT_CONTACT_ADDED = 2;
    public static final String NEW_CONTACT_ID = "id";
    public static final String NEW_CONTACT_NAME = "name";
    public static final String NEW_CONTACT_TELEPHONE = "telephone";
    public static final String NEW_CONTACT_EMAIL = "email";

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

                if(checkFields(name, telephone, email)){
                    List<Contact> contacts = ContactFile.getContacts(AddContact.this);
                    int newId;
                    //Get last id from list
                    if(contacts.size()==0){
                        newId=0;
                    }else{
                        newId = contacts.get(contacts.size()-1).getId()+1;
                    }

                    //Return data to main activity
                    Intent intent = new Intent();
                    intent.putExtra(NEW_CONTACT_ID, String.valueOf(newId));
                    intent.putExtra(NEW_CONTACT_NAME, name);
                    intent.putExtra(NEW_CONTACT_TELEPHONE, telephone);
                    intent.putExtra(NEW_CONTACT_EMAIL, email);
                    setResult(RESULT_CONTACT_ADDED, intent);
                    finish();
                }
            }
        });
    }

    private boolean checkFields(String name, String telephone, String email){
        boolean result = true;

        if(name.isEmpty()){
            edt_ContactName.setError("El nombre no puede estar vacío");
            result = false;
        }

        if(telephone.isEmpty()){
            edt_ContactTelephone.setError("El nº de teléfono no puede estar vacío");
            result = false;
        }

        if(!Pattern.matches("^([0-9]| )*", telephone)){
            edt_ContactTelephone.setError("Formato de teléfono no correcto");
            result = false;
        }

        if(!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_ContactEmail.setError("Formato de email no correcto");
            result = false;
        }

        return result;
    }
}
