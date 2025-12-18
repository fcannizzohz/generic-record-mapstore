package com.hazelcast.fcannizzohz.mapstoredemo;

import com.google.gson.Gson;
public record Gee(String id, String name) {

    public String toJson() {
        Gson gson = new Gson();
        return (gson.toJson(this));
    }

    public static Gee fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Gee.class);
    }
}
