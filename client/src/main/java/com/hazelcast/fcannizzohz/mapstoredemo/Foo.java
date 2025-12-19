package com.hazelcast.fcannizzohz.mapstoredemo;

/**
 * Message variant carrying a {@link String} payload.
 */
public class Foo
        extends Message<String> {
    /**
     * Creates a new {@code Foo} message.
     *
     * @param id the id
     * @param payload the string payload
     */
    public Foo(int id, String payload) {
        super(id, payload);
    }
}
