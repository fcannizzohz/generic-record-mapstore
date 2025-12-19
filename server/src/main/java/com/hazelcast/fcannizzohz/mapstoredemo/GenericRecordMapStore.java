package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.MapLoaderLifecycleSupport;
import com.hazelcast.map.MapStore;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;

import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class GenericRecordMapStore
        implements MapStore<String, GenericRecord>, MapLoaderLifecycleSupport {

    private final Db db;
    private Serde serde;

    GenericRecordMapStore(Db db) {
        this.db = db;
    }

    GenericRecordMapStore(Db db, Serde serde) {
        this.serde = serde;
        this.db = db;
    }

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        if (this.serde != null) {
            return;
        }
        this.serde = new SerializationServiceSerde(hazelcastInstance);
        System.out.println("initialised with serde " + serde);
    }

    @Override
    public void store(String s, GenericRecord v) {
        System.out.println("storing " + s + ": " + v);
        db.store(s, serde.toBytes(v));
    }

    @Override
    public void storeAll(Map<String, GenericRecord> map) {
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
    public GenericRecord load(String s) {
        byte[] o = db.load(s);
        return serde.fromBytes(o);
    }

    @Override
    public Map<String, GenericRecord> loadAll(Collection<String> keys) {
        System.out.println("loading all: " + keys);
        return keys.stream().map(k -> Map.entry(k, db.load(k))).filter(e -> e.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, e -> serde.fromBytes(e.getValue()), (a, _) -> a));
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
