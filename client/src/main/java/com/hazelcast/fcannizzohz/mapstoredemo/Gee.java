package com.hazelcast.fcannizzohz.mapstoredemo;

import com.google.gson.Gson;

/**
 * Simple DTO represented as a Java record and serialized to/from JSON via Gson.
 *
 * @param id identifier
 * @param name name
 */
public record Gee(String id, String name) {

    /**
     * Parses a JSON string into a {@link Gee} instance.
     *
     * @param jsonString the JSON content
     * @return parsed {@link Gee}
     */
    public static Gee fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Gee.class);
    }

    /**
     * Serializes this instance to a JSON string.
     *
     * @return JSON representation
     */
    public String toJson() {
        Gson gson = new Gson();
        return (gson.toJson(this));
    }
}
