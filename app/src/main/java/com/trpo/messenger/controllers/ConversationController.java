package com.trpo.messenger.controllers;

import com.trpo.messenger.views.AttachImageFragment;
import com.trpo.messenger.views.AttachSoundFragment;
import com.trpo.messenger.views.AttachVideoFragment;

public class ConversationController {

    void attach(int attachID) {
        switch (attachID) {
            case 0: new AttachVideoFragment();
                break;
            case 1: new AttachImageFragment();
                break;
            case 2: new AttachSoundFragment();
        }
    }
}
