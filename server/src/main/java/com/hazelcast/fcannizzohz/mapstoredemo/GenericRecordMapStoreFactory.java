package com.hazelcast.fcannizzohz.mapstoredemo;

import com.esotericsoftware.kryo.Kryo;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.MapStore;
import com.hazelcast.map.MapStoreFactory;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;
import com.hazelcast.spi.impl.SerializationServiceSupport;

import java.util.Properties;

public class GenericRecordMapStoreFactory implements MapStoreFactory<String, GenericRecord> {
    @Override
    public MapStore<String, GenericRecord> newMapStore(String map, Properties properties) {
//        HazelcastInstance hz = Hazelcast.bootstrappedInstance();
//        if(hz instanceof SerializationServiceSupport) {
//            return new GenericRecordMapStore(Db.getInstance(), new SerializationServiceSerde(hz));
//        }
//        return new GenericRecordMapStore(Db.getInstance());
                return new GenericRecordMapStore(Db.getInstance(), new KryoSerde());
    }
}
