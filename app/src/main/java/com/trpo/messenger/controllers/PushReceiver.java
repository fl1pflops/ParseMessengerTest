package com.trpo.messenger.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.trpo.messenger.R;
import com.trpo.messenger.views.ConversationFragment;
import com.trpo.messenger.views.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class PushReceiver extends BroadcastReceiver {
    private final String TAG = "asd";
    private String from = "";
    private String msg = "";
    @Override
    public void onReceive(Context ctx, Intent intent) {
        Log.i(TAG, "PUSH RECEIVED!!!");

        try {
            String action = intent.getAction();
            String channel = intent.getExtras().getString(ServerController.getCurrentUser().getName().replace("@", ".").replace(".", ""));
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
            Iterator itr = json.keys();
            while (itr.hasNext()) {
                String key = (String) itr.next();
                Log.d(TAG, "..." + key + " => " + json.getString(key));
                if(key.equals("message")){
                    msg = json.getString(key);
                }
                if(key.equals("from")){
                    from = json.getString(key);
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

        Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(),
                R.drawable.icon);

        Intent launchActivity = new Intent(ctx, MainActivity.class);
        launchActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        launchActivity.putExtra("fragment","1");
        launchActivity.putExtra("contact",from);

        PendingIntent pi = PendingIntent.getActivity(ctx, 0, launchActivity, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification noti = new NotificationCompat.Builder(ctx)
                .setContentTitle("New message from ")
                .setContentText(msg)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(icon)
                .setContentIntent(pi)
                .setAutoCancel(false)
                .build();



        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("New message from " + from)
                .setContentText(msg)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pi);


        NotificationManager nm = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1,mBuilder.build());
    }
}
