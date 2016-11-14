package com.danielacedo.introduccionadatos_ficheros.Ejercicio1;

import android.content.Context;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 14/11/2016.
 */

public class ContactFile {
    private static final String FILE_PATH = "contacts.json";

    public static boolean saveContact(Context context, Contact contact){
        boolean ok = true;
        JSONObject json = null;

        try{
            json = getJSONObject(context);
            JSONArray jsonArray = json.getJSONArray("contacts");
            jsonArray.put(contact.toJSONObject());
            File file = new File(context.getFilesDir(), FILE_PATH);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(json.toString(1));
            writer.close();
        }catch(JSONException ex){
            ok = false;
        }catch(IOException ex){

        }

        return ok;
    }

    public static List<Contact> getContacts(Context context){
        List<Contact> contacts = new ArrayList<Contact>();

        JSONObject json = getJSONObject(context);
        try {
            JSONArray array = json.getJSONArray("contacts");

            for(int i = 0; i< array.length(); i++){
                int id = array.getJSONObject(i).getInt("id");
                String name = array.getJSONObject(i).getString("name");
                String telephone = array.getJSONObject(i).getString("telephoneNumber");
                String email = array.getJSONObject(i).getString("email");

                contacts.add(new Contact(id, name, telephone, email));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contacts;
    }


    private static JSONObject getJSONObject(Context context){
        JSONObject json = null;
        File file = new File(context.getFilesDir(), FILE_PATH);

        if(file.exists()){
            BufferedReader reader;

            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                StringBuffer buffer = new StringBuffer();

                while((line = reader.readLine()) != null){
                    buffer.append(line+"\n");
                }

                reader.close();

                json = new JSONObject(buffer.toString());

            } catch (IOException e) {
                e.printStackTrace();
            } catch(JSONException e){

            }

        }else{
            try {
                file.createNewFile();
                json = new JSONObject();
                JSONArray array = new JSONArray();
                json.put("contacts", array);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
                writer.write(json.toString(1));
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch(JSONException e){

            }
        }


        return json;
    }
}
