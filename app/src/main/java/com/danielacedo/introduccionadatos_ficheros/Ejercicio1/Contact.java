package com.danielacedo.introduccionadatos_ficheros.Ejercicio1;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel on 14/11/2016.
 */

public class Contact {
    private int id;
    private String name;
    private String telephoneNumber;
    private String email;


    public Contact(int id, String name, String telephoneNumber, String email){
        this.id = id;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public JSONObject toJSONObject(){
        JSONObject json = new JSONObject();

        try {
            json.put("id", getId());
            json.put("name",getName());
            json.put("telephoneNumber", getTelephoneNumber());
            json.put("email", getEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
