package com.trpo.messenger.controllers;

import com.trpo.messenger.models.Contact;

public class ContactController {
    private Contact contact;

    void updateContact() {
        ServerController.updateContact(contact);
    }
}
