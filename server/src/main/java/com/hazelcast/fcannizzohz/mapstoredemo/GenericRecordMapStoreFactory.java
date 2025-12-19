package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.MapStore;
import com.hazelcast.map.MapStoreFactory;
import com.hazelcast.nio.serialization.genericrecord.GenericRecord;
import com.hazelcast.spi.impl.SerializationServiceSupport;

import java.util.Properties;

/**
 * Factory that creates {@link GenericRecordMapStore} instances for maps storing {@link GenericRecord} values.
 *
 * <p>Currently returns a map-store backed by the in-memory {@link Db} and using {@link KryoSerde} for
 * serialization.</p>
 */
public class GenericRecordMapStoreFactory
        implements MapStoreFactory<String, GenericRecord> {

    enum SerdeImpl {
        kryo, hazelcast_serialization
    }

    /**
     * Creates a new {@link GenericRecordMapStore} for the given map name.
     *
     * @param map the name of the map
     * @param properties map-store configuration properties. Property "serde.implementation" (values "kryo", "hazelcast_serialization") used to detect the serialization to byte[] mechanism.
     * @return a map-store instance
     */
    @Override
    public MapStore<String, GenericRecord> newMapStore(String map, Properties properties) {
        String pImpl = properties.getProperty("serde.implementation", SerdeImpl.kryo.toString());
        SerdeImpl serdeImpl = SerdeImpl.valueOf(pImpl);
        Serde impl = null;
        switch (serdeImpl) {
            case kryo: impl = new KryoSerde(); break;
            case hazelcast_serialization: {
                HazelcastInstance hz = Hazelcast.bootstrappedInstance();
                if(hz instanceof SerializationServiceSupport) {
                    impl = new SerializationServiceSerde(hz);
                } else {
                    throw new IllegalArgumentException("Unable to access hz instance: " + hz);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unknown serde: " + serdeImpl);
        }
        return new GenericRecordMapStore(Db.getInstance(), impl);
    }
}
