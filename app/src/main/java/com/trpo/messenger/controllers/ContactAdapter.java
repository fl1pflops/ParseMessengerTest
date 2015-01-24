package com.trpo.messenger.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.trpo.messenger.R;
import com.trpo.messenger.models.Contact;
import com.trpo.messenger.models.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactAdapter extends ArrayAdapter<Contact> {

    //HashMap<Message, Integer> mIdMap = new HashMap<Message, Integer>();
    List<Contact> contacts = new ArrayList<Contact>();
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    private LayoutInflater layoutInflater;

    public ContactAdapter(Context context, int textViewResourceId, List<Contact> objects) {
        super(context, textViewResourceId, objects);
        contacts = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = contacts.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.conversation_item, parent, false);
        }

        ((TextView) convertView.findViewById(R.id.text)).setText(contact.getName());

        return convertView;
    }
}
