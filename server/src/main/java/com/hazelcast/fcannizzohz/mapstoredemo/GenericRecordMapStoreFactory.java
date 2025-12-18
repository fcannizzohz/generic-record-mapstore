package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.MapLoader;
import com.hazelcast.map.MapStore;
import com.hazelcast.map.MapStoreFactory;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;
import com.hazelcast.spi.impl.SerializationServiceSupport;

import java.util.Properties;

public class GenericRecordMapStoreFactory implements MapStoreFactory<String, GenericRecord> {
    @Override
    public MapStore<String, GenericRecord> newMapStore(String s, Properties properties) {
        HazelcastInstance hz = Hazelcast.bootstrappedInstance();
        if(hz instanceof SerializationServiceSupport) {
            return new GenericRecordMapStore(Db.getInstance(), ((SerializationServiceSupport) hz).getSerializationService());
        }
        return new GenericRecordMapStore(Db.getInstance());
    }
}
