package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.nio.serialization.compact.CompactReader;
import com.hazelcast.nio.serialization.compact.CompactSerializer;
import com.hazelcast.nio.serialization.compact.CompactWriter;

/**
 * Hazelcast Compact serializer for {@link Foo}.
 */
public class FooSer
        implements CompactSerializer<Foo> {
    /**
     * Reads a {@link Foo} from the compact format.
     */
    @Override
    public Foo read(CompactReader compactReader) {
        return new Foo(compactReader.readInt32("id"), compactReader.readString("payload"));
    }

    /**
     * Writes a {@link Foo} to the compact format.
     */
    @Override
    public void write(CompactWriter compactWriter, Foo foo) {
        compactWriter.writeInt32("id", foo.getId());
        compactWriter.writeString("payload", foo.getPayload());
    }

    /** {@inheritDoc} */
    @Override
    public String getTypeName() {
        return Foo.class.getName();
    }

    /** {@inheritDoc} */
    @Override
    public Class<Foo> getCompactClass() {
        return Foo.class;
    }
}
