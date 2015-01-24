package com.trpo.messenger.models;

public abstract class User {
    private String name;
    private String email;
    private String id;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        User u = (User) o;
        return u.getId().equals(id);
    }
}
