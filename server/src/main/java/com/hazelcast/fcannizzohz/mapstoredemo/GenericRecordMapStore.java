package com.hazelcast.fcannizzohz.mapstoredemo;

import com.google.gson.Gson;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.internal.serialization.SerializationService;
import com.hazelcast.internal.serialization.impl.HeapData;
import com.hazelcast.map.MapLoaderLifecycleSupport;
import com.hazelcast.map.MapStore;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;
import com.hazelcast.spi.impl.SerializationServiceSupport;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

public class GenericRecordMapStore implements MapStore<String, GenericRecord>, MapLoaderLifecycleSupport {

    private final Db db;
    private SerializationService serializationService;

    GenericRecordMapStore(Db db) {
        this.db = db;
    }

    GenericRecordMapStore(Db db, SerializationService serializationService) {
        this.serializationService = serializationService;
        this.db = db;
    }

    @Override
    public void init(HazelcastInstance hazelcastInstance, Properties properties, String mapName) {
        if(serializationService != null) {
            return;
        }
        if (hazelcastInstance instanceof SerializationServiceSupport) {
            serializationService = ((SerializationServiceSupport) hazelcastInstance).getSerializationService();
        }
        System.out.println("initialised with serializationService " + serializationService);
    }

    @Override
    public void store(String s, GenericRecord v) {
        System.out.println("storing " + s + ": " + v);
        Data data = Objects.requireNonNull(serializationService).toData(v);
        db.store(s, data.toByteArray());
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
        return toGenericRecord(s, o);
    }

    @Override
    public Map<String, GenericRecord> loadAll(Collection<String> keys) {
        System.out.println("loading all: " + keys);
        return keys.stream()
                   .map(k -> Map.entry(k, db.load(k)))
                   .filter(e -> e.getValue() != null)
                   .collect(Collectors.toMap(
                           Map.Entry::getKey,
                           e -> toGenericRecord(e.getKey(), e.getValue()),
                           (a, _) -> a
                   ));
    }

    private GenericRecord toGenericRecord(String key, byte[] o) {
        if (o == null) {
            return null;
        }
        Data data = new HeapData(o);
        Object obj = serializationService.toObject(data);
        if (!(obj instanceof GenericRecord gr)) {
            throw new IllegalStateException(
                    "Expected GenericRecord for key=" + key + " but got " +
                            (obj == null ? "null" : obj.getClass().getName())
            );
        }
        return gr;
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
