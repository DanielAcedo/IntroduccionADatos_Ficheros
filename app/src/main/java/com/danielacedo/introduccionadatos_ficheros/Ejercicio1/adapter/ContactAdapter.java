package com.danielacedo.introduccionadatos_ficheros.Ejercicio1.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.Contact;
import com.danielacedo.introduccionadatos_ficheros.Ejercicio1.ContactFile;
import com.danielacedo.introduccionadatos_ficheros.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 14/11/2016.
 */

public class ContactAdapter extends ArrayAdapter<Contact> {
    private Context context;

    public ContactAdapter(Context context){
        super(context, R.layout.contact_layout, ContactFile.getContacts(context));
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ContactHolder holder;

        if(view == null){
            holder = new ContactHolder();
            view = LayoutInflater.from(context).inflate(R.layout.contact_layout, null);

            holder.txv_ContactName = (TextView)view.findViewById(R.id.txv_contactlist_name);
            holder.txv_ContactTelephone = (TextView)view.findViewById(R.id.txv_contactlist_telephone);
            holder.txv_ContactEmail = (TextView)view.findViewById(R.id.txv_contactlist_email);
            holder.btn_RemoveContact = (ImageButton) view.findViewById(R.id.btn_RemoveContact);
            holder.btn_RemoveContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(getItem(position));
                    ContactFile.overwriteContacts(getContext(), getItems());
                    notifyDataSetChanged();
                }
            });
            view.setTag(holder);
        }else{
            holder = (ContactHolder) view.getTag();
        }

        holder.txv_ContactName.setText(getItem(position).getName());
        holder.txv_ContactTelephone.setText(getItem(position).getTelephoneNumber());
        holder.txv_ContactEmail.setText(getItem(position).getEmail());

        return view;
    }

    private List<Contact> getItems(){
        List<Contact> contacts = new ArrayList<Contact>();

        for(int i = 0; i< getCount(); i++){
            contacts.add(getItem(i));
        }

        return contacts;
    }

    static class ContactHolder{
        TextView txv_ContactName, txv_ContactTelephone, txv_ContactEmail;
        ImageButton btn_RemoveContact;
    }

    @Override
    public void add(Contact object) {
        ContactFile.saveContact(getContext(), object);
        super.add(object);
    }
}
