package com.nekoid.smektuber.models;

import org.json.JSONObject;

public class ExtracurricularModel extends Models {

    public int id;

    public String name;

    public String description;

    public String photo;

    public String createdAt;

    public String updatedAt;

    public ExtracurricularModel(int id, String name, String description, String photo, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ExtracurricularModel fromJson(JSONObject json) {
        return new ExtracurricularModel(
                json.optInt("id"),
                json.optString("extracurricular_name"),
                json.optString("extracurricular_description"),
                json.optString("extracurricular_logo"),
                json.optString("created_at"),
                json.optString("updated_at")
        );
    }
}