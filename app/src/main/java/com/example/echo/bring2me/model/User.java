package com.example.echo.bring2me.model;

public class User {
    private final String name;
    private final String email;
    private final String uid;
    private final String created_at;

    /**
     */
    public User(String name, String email, String uid, String created_at) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getCreated_at() {
        return created_at;
    }
}
