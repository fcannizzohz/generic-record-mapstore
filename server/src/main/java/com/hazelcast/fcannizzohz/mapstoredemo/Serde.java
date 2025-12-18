package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.nio.serialization.genericrecord.GenericRecord;

public interface Serde {

    byte[] toBytes(GenericRecord obj);
    GenericRecord fromBytes(byte[] bytes);
}
