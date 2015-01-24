package com.trpo.messenger.controllers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.trpo.messenger.R;
import com.trpo.messenger.models.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    //HashMap<Message, Integer> mIdMap = new HashMap<Message, Integer>();
    List<Message> messages = new ArrayList<Message>();
    public static final int DIRECTION_INCOMING = 0;
    public static final int DIRECTION_OUTGOING = 1;
    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context, int textViewResourceId, List<Message> objects, Fragment activity) {
        super(context, textViewResourceId, objects);
        layoutInflater = activity.getActivity().getLayoutInflater();
        messages = objects;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int i) {
        if (messages.get(i).getFrom().equals(ServerController.getCurrentUser().getId())) return 0;
        else return 1;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        int direction = getItemViewType(i);
        //show message on left or right, depending on if
        //it's incoming or outgoing
        if (convertView == null) {
            int res = 0;
            if (direction == DIRECTION_INCOMING) {
                res = R.layout.message_right;
            } else if (direction == DIRECTION_OUTGOING) {
                res = R.layout.message_left;
            }
            convertView = layoutInflater.inflate(res, viewGroup, false);
        }
        TextView txtMessage = (TextView) convertView.findViewById(R.id.txtMessage);
        txtMessage.setText(messages.get(i).getMessage());
        return convertView;
    }
}
