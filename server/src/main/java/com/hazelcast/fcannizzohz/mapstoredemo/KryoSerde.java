package com.hazelcast.fcannizzohz.mapstoredemo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * {@link Serde} implementation based on the Kryo serialization library.
 *
 * <p>Uses a {@link ThreadLocal} {@link Kryo} instance configured to not require registration and with
 * a {@link DefaultInstantiatorStrategy} to support classes without no-arg constructors.</p>
 */
public final class KryoSerde
        implements Serde {

    private final ThreadLocal<Kryo> kryoTL;

    /**
     * Creates a new instance with lazy-initialized, thread-local {@link Kryo} instances.
     */
    public KryoSerde() {
        this.kryoTL = ThreadLocal.withInitial(() -> {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
            return kryo;
        });
    }

    /** {@inheritDoc} */
    @Override
    public byte[] toBytes(GenericRecord v) {
        if (v == null) {
            return null;
        }
        Kryo kryo = kryoTL.get();
        try (Output out = new Output(1024, -1)) {
            kryo.writeClassAndObject(out, v);
            return out.toBytes();
        }
    }

    /** {@inheritDoc} */
    @Override
    public GenericRecord fromBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        Kryo kryo = kryoTL.get();
        try (Input in = new Input(bytes)) {
            Object obj = kryo.readClassAndObject(in);
            if (!(obj instanceof GenericRecord gr)) {
                throw new IllegalStateException("Expected GenericRecord, got " + (obj == null ? "null" : obj.getClass().getName()));
            }
            return gr;
        }
    }
}
