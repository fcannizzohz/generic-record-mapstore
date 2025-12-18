package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

public class BarSer implements CompactSerializer<Bar> {
    @Override
    public Bar read(CompactReader compactReader) {
        return new Bar(compactReader.readInt32("id"), compactReader.readInt64("payload"));
    }

    @Override
    public void write(CompactWriter compactWriter, Bar bar) {
        compactWriter.writeInt32("id", bar.getId());
        compactWriter.writeInt64("payload", bar.getPayload());
    }

    @Override
    public String getTypeName() {
        return Bar.class.getName();
    }

    @Override
    public Class<Bar> getCompactClass() {
        return Bar.class;
    }
}
