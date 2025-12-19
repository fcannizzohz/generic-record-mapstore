package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastJsonValue;
import com.hazelcast.map.MapLoaderLifecycleSupport;
import com.hazelcast.map.MapStore;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class JsonValueMapStore
        implements MapStore<String, HazelcastJsonValue>, MapLoaderLifecycleSupport {

    private Db db;

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        db = Db.getInstance();
    }

    @Override
    public void store(String s, HazelcastJsonValue v) {
        System.out.println("storing " + s + ": " + v);
        db.store(s, v.getValue().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void storeAll(Map<String, HazelcastJsonValue> map) {
        System.out.println("storing all data");
        map.entrySet().iterator().forEachRemaining(e -> this.store(e.getKey(), e.getValue()));
    }

    @Override
    public void delete(String s) {
        db.delete(s);
    }

    @Override
    public void deleteAll(Collection<String> collection) {
        collection.forEach(db::delete);
    }

    @Override
    public HazelcastJsonValue load(String s) {
        byte[] o = db.load(s);
        if (o == null) {
            return null;
        }
        return new HazelcastJsonValue(new String(o, StandardCharsets.UTF_8));
    }

    @Override
    public Map<String, HazelcastJsonValue> loadAll(Collection<String> keys) {
        System.out.println("loading all: " + keys);
        return keys.stream().map(k -> Map.entry(k, db.load(k))).filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, e -> new HazelcastJsonValue(new String(e.getValue())), (a, _) -> a));
    }

    @Override
    public Iterable<String> loadAllKeys() {
        return db.keySet();
    }

    @Override
    public void destroy() {
        db.clear();
    }
}
