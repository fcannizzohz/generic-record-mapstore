package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.nio.serialization.genericrecord.GenericRecord;

/**
 * Simple serializer/deserializer (serde) abstraction for Hazelcast {@link GenericRecord} values.
 * Implementations are responsible for converting generic records to a byte array and back.
 */
public interface Serde {

    /**
     * Serializes the given {@link GenericRecord} to a byte array.
     *
     * @param obj the record to serialize; may be {@code null}
     * @return the serialized bytes, or {@code null} if the input is {@code null}
     */
    byte[] toBytes(GenericRecord obj);

    /**
     * Deserializes the given bytes into a {@link GenericRecord} instance.
     *
     * @param bytes the serialized representation; may be {@code null} or empty
     * @return the deserialized {@link GenericRecord}, or {@code null} if input is {@code null} or empty
     * @throws IllegalStateException if the bytes do not represent a {@link GenericRecord}
     */
    GenericRecord fromBytes(byte[] bytes);
}
