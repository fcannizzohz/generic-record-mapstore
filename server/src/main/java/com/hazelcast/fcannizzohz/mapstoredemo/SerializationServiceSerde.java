package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.internal.serialization.SerializationService;
import com.hazelcast.internal.serialization.impl.HeapData;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;
import com.hazelcast.spi.impl.SerializationServiceSupport;

import java.util.Objects;

public class SerializationServiceSerde implements Serde {

    private final SerializationService serializationService;

    public SerializationServiceSerde(HazelcastInstance hazelcastInstance) {
        this(((SerializationServiceSupport)hazelcastInstance).getSerializationService());
    }

    public SerializationServiceSerde(SerializationService serializationService) {
        this.serializationService = serializationService;
    }

    @Override
    public byte[] toBytes(GenericRecord v) {
        SerializationService ss = Objects.requireNonNull(serializationService);
        Data data = ss.toData(v);
        return data.toByteArray();
    }

    @Override
    public GenericRecord fromBytes(byte[] bytes) {
        if(Objects.isNull(bytes) || bytes.length == 0) {
            return null;
        }
        Data data = new HeapData(bytes);
        Object obj = serializationService.toObject(data);
        if (!(obj instanceof GenericRecord gr)) {
            throw new IllegalStateException(
                    "Expected GenericRecord but got " +
                            (obj == null ? "null" : obj.getClass().getName())
            );
        }
        return gr;
    }
}
