package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.nio.serialization.genericrecord.GenericRecord;

public class Foo extends Message<String> {
    public Foo(int id, String payload) {
        super(id, payload);
    }
}
