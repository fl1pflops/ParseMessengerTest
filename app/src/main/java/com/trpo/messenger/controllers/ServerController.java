package com.trpo.messenger.controllers;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.parse.*;
import com.squareup.otto.Bus;
import com.trpo.messenger.models.Account;
import com.trpo.messenger.models.Contact;
import com.trpo.messenger.models.Message;
import com.trpo.messenger.views.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerController extends Application{

    private static Context context;
    private static Account currentUser;
    public static Bus bus;
    private static AccountManagerController accountManagerController;

    public void onCreate() {
        super.onCreate();
        accountManagerController = new AccountManagerController();
        context = getApplicationContext();

        bus = new Bus();
        Parse.initialize(getApplicationContext(), "", "");

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("asd", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("asd", "failed to subscribe for push", e);
                }
            }
        });

        accountManagerController.readFromSharedPreferences(context);

        if (currentUser != null) {
            startMainActivity();
        }
    }

    public static void signUp(final Account user, final String pass) {
        final ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user.getName());
        parseUser.setPassword(pass);
        parseUser.setEmail(user.getEmail());

        parseUser.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    user.setId(parseUser.getObjectId());
                    currentUser = user;
                    accountManagerController.saveToSharedPreferences(context);

                    startMainActivity();
                    Log.d("asd", "SUCCESS");
                } else {
                    if (e.getCode() == 202) {
                        ParseUser.logInInBackground(user.getEmail(), pass, new LogInCallback() {
                            public void done(ParseUser userFromParse, ParseException e) {
                                if (userFromParse != null) {
                                    currentUser = user;
                                    user.setId(userFromParse.getObjectId());
                                    accountManagerController.saveToSharedPreferences(context);

                                    startMainActivity();
                                } else {
                                    Toast.makeText(ServerController.getAppContext(), "Something goes wrong!", Toast.LENGTH_SHORT).show();
                                    Log.d("asd", e.getMessage());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ServerController.getAppContext(), "Error", Toast.LENGTH_SHORT).show();
                        Log.d("asd", e.getMessage());
                    }
                }
            }
        });
    }

    public static void sendMessage(Message message) {
        ParseObject newMessage = new ParseObject("Message");
        newMessage.put("from", message.getFrom());
        newMessage.put("to", message.getTo());
        newMessage.put("message", message.getMessage());
        newMessage.saveInBackground();
    }

    public static void getMessages(final Contact withUser) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
        query.whereContainedIn("to", new ArrayList<String>(Arrays.asList(new String[]{withUser.getId(), currentUser.getId()})));
        query.whereContainedIn("from", new ArrayList<String>(Arrays.asList(new String[] { withUser.getId(), currentUser.getId() })));
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    List<Message> messages = new ArrayList<Message>();

                    for (ParseObject o: objects) {
                        Message m = new Message(o.getString("from"), o.getString("to"), o.getString("message"));
                        m.setId(o.getObjectId());
                        messages.add(m);
                    }

                    bus.post(messages.toArray(new Message[messages.size()]));
                }
            }
        });
    }

    public static void getContacts() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("objectId", currentUser.getId());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    List<Contact> users = new ArrayList<Contact>();

                    for (ParseUser o: objects) {
                        Contact u = new Contact(o.getUsername(), o.getEmail());
                        u.setId(o.getObjectId());
                        users.add(u);
                    }

                    bus.post(users.toArray(new Contact[users.size()]));
                }
            }
        });
    }

    private static void startMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static Context getAppContext() {
        return ServerController.context;
    }

    public static Account getUser() {
        return currentUser;
    }

    static void updateContact(Contact contact) {
    }

    public static Account getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Account currentUser) {
        ServerController.currentUser = currentUser;
    }
}
