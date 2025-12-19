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

/**
 * {@link MapStore} implementation for maps whose values are {@link HazelcastJsonValue}.
 *
 * <p>JSON strings are stored as UTF-8 encoded bytes in the {@link Db}.</p>
 */
public class JsonValueMapStore
        implements MapStore<String, HazelcastJsonValue>, MapLoaderLifecycleSupport {

    private Db db;

    /**
     * Initializes the store using the singleton {@link Db} instance.
     */
    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        db = Db.getInstance();
    }

    /**
     * Persists a single JSON value.
     */
    @Override
    public void store(String s, HazelcastJsonValue v) {
        System.out.println("storing " + s + ": " + v);
        db.store(s, v.getValue().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Persists all values by delegating to {@link #store(String, HazelcastJsonValue)}.
     */
    @Override
    public void storeAll(Map<String, HazelcastJsonValue> map) {
        System.out.println("storing all data");
        map.entrySet().iterator().forEachRemaining(e -> this.store(e.getKey(), e.getValue()));
    }

    /**
     * Deletes a single entry.
     */
    @Override
    public void delete(String s) {
        db.delete(s);
    }

    /**
     * Deletes all provided keys.
     */
    @Override
    public void deleteAll(Collection<String> collection) {
        collection.forEach(db::delete);
    }

    /**
     * Loads and reconstructs a {@link HazelcastJsonValue} from stored bytes.
     */
    @Override
    public HazelcastJsonValue load(String s) {
        byte[] o = db.load(s);
        if (o == null) {
            return null;
        }
        return new HazelcastJsonValue(new String(o, StandardCharsets.UTF_8));
    }

    /**
     * Loads all requested keys.
     */
    @Override
    public Map<String, HazelcastJsonValue> loadAll(Collection<String> keys) {
        System.out.println("loading all: " + keys);
        return keys.stream().map(k -> Map.entry(k, db.load(k))).filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, e -> new HazelcastJsonValue(new String(e.getValue())), (a, _) -> a));
    }

    /**
     * Returns all known keys.
     */
    @Override
    public Iterable<String> loadAllKeys() {
        return db.keySet();
    }

    /**
     * Clears the underlying storage.
     */
    @Override
    public void destroy() {
        db.clear();
    }
}
