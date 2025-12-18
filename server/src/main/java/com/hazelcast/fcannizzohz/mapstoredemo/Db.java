package com.hazelcast.fcannizzohz.mapstoredemo;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class Db {

    private static Db instance = new Db();

    public static Db getInstance() {
        return instance;
    }

    private final ConcurrentHashMap<String, byte[]> map = new ConcurrentHashMap<>();

    private Db() {
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
