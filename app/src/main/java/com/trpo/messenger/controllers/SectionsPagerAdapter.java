package com.trpo.messenger.controllers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import com.trpo.messenger.views.ContactsFragment;
import com.trpo.messenger.views.ConversationFragment;

import java.util.Locale;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch(position) {
            case 0: return ContactsFragment.newInstance(position + 1);
            case 1: return ConversationFragment.newInstance(position + 1);
            default: return ConversationFragment.newInstance(position + 1);
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Contacts";
            case 1:
                return "Conversation";
        }
        return null;
    }
}
