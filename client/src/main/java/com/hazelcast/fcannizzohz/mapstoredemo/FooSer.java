package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class FooSer implements CompactSerializer<Foo> {
    @Override
    public Foo read(CompactReader compactReader) {
        return new Foo(compactReader.readInt32("id"), compactReader.readString("payload"));
    }

    @Override
    public void write(CompactWriter compactWriter, Foo foo) {
        compactWriter.writeInt32("id", foo.getId());
        compactWriter.writeString("payload", foo.getPayload());
    }

    @Override
    public String getTypeName() {
        return Foo.class.getName();
    }

    @Override
    public Class<Foo> getCompactClass() {
        return Foo.class;
    }
}
