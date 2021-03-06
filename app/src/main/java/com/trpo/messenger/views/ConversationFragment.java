package com.trpo.messenger.views;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SendCallback;
import com.squareup.otto.Subscribe;
import com.trpo.messenger.R;
import com.trpo.messenger.controllers.MessageAdapter;
import com.trpo.messenger.controllers.ServerController;
import com.trpo.messenger.models.Contact;
import com.trpo.messenger.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConversationFragment extends Fragment {
    private static Contact currentUserConversation;
    private static MessageAdapter messagesAdapter = null;
    private static final ArrayList<Message> messages = new ArrayList<Message>();
    //private static final ArrayList<String> messagesText = new ArrayList<String>();

    public static MessageAdapter getMessagesAdapter() {
        return messagesAdapter;
    }
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ConversationFragment newInstance(int sectionNumber) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ConversationFragment() {
    }

    public static void setCurrentUserConversation(Contact currentUserConversation) {
        ConversationFragment.currentUserConversation = currentUserConversation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ServerController.bus.register(this);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final EditText message = (EditText) rootView.findViewById(R.id.message);
        final Button sendMessage = (Button) rootView.findViewById(R.id.send_message);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUserConversation != null) {
                    ServerController.sendMessage(
                            new Message(ServerController.getUser().getId(), currentUserConversation.getId(), message.getText().toString())
                    );

                    JSONObject object = new JSONObject();
                    try {
                        object.put("from", ServerController.getUser().getName());
                        object.put("message", message.getText().toString());
                        //object.put("actionyAction", "actionyAction");

                        ParsePush pushMessageClient1 = new ParsePush();
                        pushMessageClient1.setData(object);
                        pushMessageClient1.setChannel(currentUserConversation.getName().replace("@",".").replace(".",""));
                        pushMessageClient1.sendInBackground(new SendCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) Toast.makeText(getActivity(), "Something goes wrong", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    message.setText("");
                    ServerController.getMessages(currentUserConversation);
                }
            }
        });

        ListView messagesView = (ListView) rootView.findViewById(R.id.messages_list);

        messagesAdapter = new MessageAdapter(getActivity(), android.R.layout.simple_list_item_1, messages, this);
        messagesView.setAdapter(messagesAdapter);

        if (currentUserConversation != null) {
            ServerController.getMessages(currentUserConversation);
        }

        return rootView;
    }

    @Subscribe
    public void getMessages(Message[] messagesFromParse) {
        //messagesText.clear();
        messages.clear();
        for (Message m: messagesFromParse) {
            if (!messages.contains(m) && (m.getFrom().equals(currentUserConversation.getId()) || m.getTo().equals(currentUserConversation.getId()))) {
                messages.add(m);
                //messagesText.add(m.getMessage());
            }
        }

        Log.d("asd", "NEW MESSAGES " + messagesFromParse.length);

        ConversationFragment.getMessagesAdapter().notifyDataSetChanged();
    }

    public static void clearMessages() {
        messages.clear();
        if (messagesAdapter != null) messagesAdapter.notifyDataSetChanged();
    }
}