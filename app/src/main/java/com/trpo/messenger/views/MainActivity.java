package com.trpo.messenger.views;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.trpo.messenger.R;
import com.trpo.messenger.controllers.AccountManagerController;
import com.trpo.messenger.controllers.SectionsPagerAdapter;
import com.trpo.messenger.controllers.ServerController;
import com.trpo.messenger.models.Contact;


public class MainActivity extends Activity {
    private static SectionsPagerAdapter mSectionsPagerAdapter;
    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        String extra = getIntent().getStringExtra("fragment");
        //Log.d("asd", "intent: " + extra);
        if (extra != null) {
            Log.d("asd", "1");
            Contact contact = ContactsFragment.getContactsAdapter().getContactByName(getIntent().getStringExtra("contact"));
            Log.d("asd", contact.getName());
            AccountManagerController.readFromSharedPreferences(this);

            ConversationFragment.clearMessages();
            ConversationFragment.setCurrentUserConversation(contact);
            ServerController.getMessages(contact);
            MainActivity.getmViewPager().setCurrentItem(1);
        } else ServerController.getContacts();


        /*if (intentExtra != null) {
            if (intentExtra.equals("1")) {
                Log.d("asd", "1");
                //mViewPager.setCurrentItem(1);
                //ConversationFragment.setCurrentUserConversation(contacts.get(position));
                //ServerController.getMessages(contacts.get(position));
                MainActivity.getmViewPager().setCurrentItem(1);
                //ConversationFragment.clearMessages();
            }
        }*/

    }

    @Override
    protected void onNewIntent(Intent intent) {
        String extra = getIntent().getStringExtra("fragment");
        Log.d("asd", "onNewIntent: " + extra);
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
        }
    }

    public static ViewPager getmViewPager() {
        return mViewPager;
    }
}
