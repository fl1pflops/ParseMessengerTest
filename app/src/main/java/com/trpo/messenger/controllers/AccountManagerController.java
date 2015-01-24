package com.trpo.messenger.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.trpo.messenger.models.Account;
import com.trpo.messenger.models.User;
import com.trpo.messenger.views.LoginActivity;

import java.util.HashMap;

public class AccountManagerController {
    public void saveToSharedPreferences(Context context) {
        if (ServerController.getCurrentUser() != null) {
            SharedPreferences sharedPref = context.getSharedPreferences("Account", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name", ServerController.getCurrentUser().getName());
            editor.putString("email", ServerController.getCurrentUser().getEmail());
            editor.putString("id", ServerController.getCurrentUser().getId());
            editor.commit();
        }
    }

    public void readFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("Account", Context.MODE_PRIVATE);

        String name = sharedPref.getString("name", null);
        String id = sharedPref.getString("id", null);
        String email = sharedPref.getString("email", null);

        if (name != null && id != null && email != null) {
            ServerController.setCurrentUser(new Account(name, email));
            ServerController.getCurrentUser().setId(id);
        }
    }
}
