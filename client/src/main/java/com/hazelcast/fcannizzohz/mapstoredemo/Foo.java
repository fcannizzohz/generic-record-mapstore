package com.hazelcast.fcannizzohz.mapstoredemo;

public class Foo
        extends Message<String> {
    public Foo(int id, String payload) {
        super(id, payload);
    }
}
