package com.hazelcast.fcannizzohz.mapstoredemo;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Very simple in-memory key-value store used by the demo map-store implementations.
 *
 * <p>This class is a thread-safe singleton backed by a {@link ConcurrentHashMap} and stores
 * raw {@code byte[]} values keyed by {@link String}.</p>
 */
public final class Db {

    private static final Db instance = new Db();
    private final ConcurrentHashMap<String, byte[]> map = new ConcurrentHashMap<>();

    private Db() {
    }

    /**
     * Returns the singleton instance.
     */
    public static Db getInstance() {
        return instance;
    }

    /**
     * Stores a value under the given key, replacing any existing value.
     *
     * @param key the key
     * @param value the value bytes (may be {@code null})
     */
    public void store(String key, byte[] value) {
        map.put(key, value);
    }

    /**
     * Loads the value for the given key.
     *
     * @param key the key
     * @return the stored bytes or {@code null} if missing
     */
    public byte[] load(String key) {
        return map.get(key);
    }

    /**
     * Removes all entries from the store.
     */
    public void clear() {
        map.clear();
    }

    /**
     * Deletes the entry associated with the given key.
     *
     * @param key the key to remove
     */
    public void delete(String key) {
        map.remove(key);
    }

    /**
     * Returns a live view of the current keys.
     *
     * @return iterable of keys
     */
    public Iterable<String> keySet() {
        return map.keySet();
    }
}
