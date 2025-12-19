package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

/**
 * Hazelcast Compact serializer for {@link Bar}.
 */
public class BarSer
        implements CompactSerializer<Bar> {
    /**
     * Reads a {@link Bar} from the compact format.
     */
    @Override
    public Bar read(CompactReader compactReader) {
        return new Bar(compactReader.readInt32("id"), compactReader.readInt64("payload"));
    }

    /**
     * Writes a {@link Bar} to the compact format.
     */
    @Override
    public void write(CompactWriter compactWriter, Bar bar) {
        compactWriter.writeInt32("id", bar.getId());
        compactWriter.writeInt64("payload", bar.getPayload());
    }

    /** {@inheritDoc} */
    @Override
    public String getTypeName() {
        return Bar.class.getName();
    }

    /** {@inheritDoc} */
    @Override
    public Class<Bar> getCompactClass() {
        return Bar.class;
    }
}
