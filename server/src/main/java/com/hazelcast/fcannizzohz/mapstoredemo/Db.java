package com.hazelcast.fcannizzohz.mapstoredemo;

import java.util.concurrent.ConcurrentHashMap;

public final class Db {

    private static final Db instance = new Db();
    private final ConcurrentHashMap<String, byte[]> map = new ConcurrentHashMap<>();

    private Db() {
    }

    public static Db getInstance() {
        return instance;
    }

    public void store(String key, byte[] value) {
        map.put(key, value);
    }

    public byte[] load(String key) {
        return map.get(key);
    }

    public void clear() {
        map.clear();
    }

    public void delete(String key) {
        map.remove(key);
    }

    public Iterable<String> keySet() {
        return map.keySet();
    }
}
