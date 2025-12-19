package com.hazelcast.fcannizzohz.mapstoredemo;

/**
 * Message variant carrying a {@link Long} payload.
 */
public class Bar
        extends Message<Long> {
    /**
     * Creates a new {@code Bar} message.
     *
     * @param id the id
     * @param payload the long payload
     */
    public Bar(int id, Long payload) {
        super(id, payload);
    }
}
