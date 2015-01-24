package com.trpo.messenger.models;

public class Message {
    private String from;
    private String to;
    private String message;
    private String id;

    public Message(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        Message m = (Message) o;
        return m.getId().equals(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
