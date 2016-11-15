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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 14/11/2016.
 */

/**
 * Helper class for managing contacts saved in a JSONFile
 * @author Daniel Acedo Calderón
 */
public class ContactFile {
    private static final String FILE_PATH = "contacts.json";

    /**
     * Adds one contact to the jsonfile
     * @param context Context
     * @param contact Contact to be saved
     * @return true if success, false if error
     */
    public static boolean saveContact(Context context, Contact contact){
        boolean ok = true;
        JSONObject json = null;

        try{
            json = getJSONObject(context);
            JSONArray jsonArray = json.getJSONArray("contacts");
            jsonArray.put(contact.toJSONObject());
            File file = new File(context.getFilesDir(), FILE_PATH);

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString(1)); //Write the json object with 1 indentation space
            writer.close();
        }catch(JSONException ex){
            ok = false;
        }catch(IOException ex){

        }

        return ok;
    }


    /**
     * Reads the JSON file and returns a List of contacts
     * @param context Context
     * @return List of contacts
     */
    public static List<Contact> getContacts(Context context){
        List<Contact> contacts = new ArrayList<Contact>();

        JSONObject json = getJSONObject(context);
        try {
            JSONArray jsonContacts = getJSONContacts(context);

            for(int i = 0; i< jsonContacts.length(); i++){
                int id = jsonContacts.getJSONObject(i).getInt("id");
                String name = jsonContacts.getJSONObject(i).getString("name");
                String telephone = jsonContacts.getJSONObject(i).getString("telephoneNumber");
                String email = jsonContacts.getJSONObject(i).getString("email");

                contacts.add(new Contact(id, name, telephone, email));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    /**
     * Adds a list of contacts to the JSONFile
     * @param context Context
     * @param contacts Contacts to be saved
     */
    public static void saveContacts(Context context, List<Contact> contacts){

        for(Contact contact : contacts){
            saveContact(context, contact);
        }
    }

    /**
     * Resets the JSON file and saves a list of contacts
     * @param context Context
     * @param contacts Contacts to be saved
     */
    public static void overwriteContacts(Context context, List<Contact> contacts){
        resetJSONFile(context);
        saveContacts(context, contacts);
    }

    private static JSONArray getJSONContacts(Context context){
        JSONObject json = getJSONObject(context);
        JSONArray contacts = null;

        try {
            contacts = json.getJSONArray("contacts");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contacts;
    }

    /**
     * Reads the current file and returns the JSONObject
     * @param context Context
     * @return JSONObject representing the file
     */
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
                json.getJSONArray("contacts"); //If trying to retrieve contact array throws an exception, this is not well formatted

            } catch (IOException e) {
                e.printStackTrace();
            } catch(JSONException e){
                json = resetJSONFile(context); //Reformat the file
            }

        }else{
            resetJSONFile(context);
        }


        return json;
    }

    /**
     * Creates a valid formatted json file overwriting if it already exits
     * @param context Context
     * @return Fresh formatted json object
     */
    private static JSONObject resetJSONFile(Context context){
        JSONObject json = null;
        File file = new File(context.getFilesDir(), FILE_PATH);

        try {
            file.createNewFile();
            json = getCleanJSONObject();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(json.toString(1));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * Returns clean formatted JSONObject
     * @return Clean JSONObject
     */
    private static JSONObject getCleanJSONObject(){
        JSONObject json = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            json.put("contacts", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
