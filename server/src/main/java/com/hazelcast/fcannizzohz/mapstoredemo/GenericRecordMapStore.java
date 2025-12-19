package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.MapLoaderLifecycleSupport;
import com.hazelcast.map.MapStore;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * {@link MapStore} implementation that persists {@link GenericRecord} values to the {@link Db}.
 *
 * <p>Values are serialized using a pluggable {@link Serde}. If no serde is provided via the constructor,
 * one is created during {@link #init(HazelcastInstance, Properties, String)} based on the running
 * Hazelcast instance.</p>
 */
public class GenericRecordMapStore
        implements MapStore<String, GenericRecord>, MapLoaderLifecycleSupport {

    private final Db db;
    private Serde serde;

    /**
     * Creates a map-store using the provided {@link Db}. The serde will be resolved during {@link #init}.
     */
    GenericRecordMapStore(Db db) {
        this.db = db;
    }

    /**
     * Creates a map-store using the provided {@link Db} and {@link Serde}.
     */
    GenericRecordMapStore(Db db, Serde serde) {
        this.serde = serde;
        this.db = db;
    }

    /**
     * Initializes the map-store. If a serde was not supplied, it will use the Hazelcast
     * {@link HazelcastInstance}'s {@link com.hazelcast.internal.serialization.SerializationService} via
     * {@link SerializationServiceSerde}.
     */
    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        if (this.serde != null) {
            return;
        }
        this.serde = new SerializationServiceSerde(hazelcastInstance);
        System.out.println("initialised with serde " + serde);
    }

    /**
     * Persists a single entry.
     */
    @Override
    public void store(String s, GenericRecord v) {
        System.out.println("storing " + s + ": " + v);
        db.store(s, serde.toBytes(v));
    }

    /**
     * Persists all provided entries by delegating to {@link #store(String, GenericRecord)}.
     */
    @Override
    public void storeAll(Map<String, GenericRecord> map) {
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
     * Loads and deserializes a single entry.
     */
    @Override
    public GenericRecord load(String s) {
        byte[] o = db.load(s);
        return serde.fromBytes(o);
    }

    /**
     * Loads and deserializes all requested entries.
     */
    @Override
    public Map<String, GenericRecord> loadAll(Collection<String> keys) {
        System.out.println("loading all: " + keys);
        return keys.stream().map(k -> Map.entry(k, db.load(k))).filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, e -> serde.fromBytes(e.getValue()), (a, _) -> a));
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
