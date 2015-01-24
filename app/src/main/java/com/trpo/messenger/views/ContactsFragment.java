package com.trpo.messenger.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.squareup.otto.Subscribe;
import com.trpo.messenger.R;
import com.trpo.messenger.controllers.ContactAdapter;
import com.trpo.messenger.controllers.ServerController;
import com.trpo.messenger.models.Contact;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ContactAdapter contactsAdapter = null;
    private static final ArrayList<Contact> contacts = new ArrayList<Contact>();
    private static final ArrayList<String> contactsText = new ArrayList<String>();

    public static ContactAdapter getContactsAdapter() {
        return contactsAdapter;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ContactsFragment newInstance(int sectionNumber) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ServerController.bus.register(this);
        View rootView = inflater.inflate(R.layout.conversation_fragment, container, false);

        ListView contactsListView = (ListView) rootView.findViewById(R.id.contact_list);

        contactsAdapter = new ContactAdapter(getActivity(), android.R.layout.simple_list_item_1, contacts);
        contactsListView.setAdapter(contactsAdapter);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ConversationFragment.setCurrentUserConversation(contacts.get(position));
                ServerController.getMessages(contacts.get(position));
                MainActivity.getmViewPager().setCurrentItem(1);
                ConversationFragment.clearMessages();
            }
        });

        return rootView;
    }

    @Subscribe
    public void getContacts(Contact[] contactsFromParse) {

        Log.d("asd", "CONTACTS " + contactsFromParse.length);

        for (Contact u: contactsFromParse) {
            if (!contacts.contains(u)) {
                contacts.add(u);
                contactsText.add(u.getName());
            }
        }

        if (contacts.size() > 0) {
            ConversationFragment.setCurrentUserConversation(contacts.get(0));
        }

        ContactsFragment.getContactsAdapter().notifyDataSetChanged();
    }
}