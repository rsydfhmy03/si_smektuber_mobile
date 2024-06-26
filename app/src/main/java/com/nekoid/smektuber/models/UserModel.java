package com.nekoid.smektuber.models;

import org.json.JSONException;
import org.json.JSONObject;

public class UserModel extends Models {

    public String name;

    public String email;

    public String role;

    public String username;

    public String avatar;

    public boolean emailVerified;

    public UserModel(String name, String email, String role, String avatar, String username, boolean emailVerified) {
        this.name = isEmpty(name);
        this.email = isEmpty(email);
        this.role = isEmpty(role);
        this.avatar = isEmpty(avatar);
        this.username = isEmpty(username);
        this.emailVerified = emailVerified;
    }

    public static UserModel fromJson(JSONObject json) throws JSONException {
        return new UserModel(
                json.getString("name"),
                json.getString("email"),
                json.getString("role"),
                json.getString("avatar"),
                json.getString("username"),
                json.getBoolean("email_verified_at")
        );
    }
}