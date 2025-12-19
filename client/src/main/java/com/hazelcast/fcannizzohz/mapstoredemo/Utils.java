package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.CompactSerializationConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastJsonValue;
import com.hazelcast.map.IMap;

/**
 * Convenience utilities for accessing demo maps on a Hazelcast client instance.
 */
public final class Utils {

    private static final HazelcastInstance hz;

    private Utils() {
    }

    /**
     * Returns the {@code plopMap}.
     */
    public static IMap<String, Plop> plopMap() {
        return hz.getMap("plopMap");
    }

    /**
     * Returns the {@code fooMap}.
     */
    public static IMap<String, Foo> fooMap() {
        return hz.getMap("fooMap");
    }

    /**
     * Returns the {@code barMap}.
     */
    public static IMap<String, Bar> barMap() {
        return hz.getMap("barMap");
    }

    /**
     * Returns the {@code geeMap} holding {@link HazelcastJsonValue} values.
     */
    public static IMap<String, HazelcastJsonValue> geeMap() {
        return hz.getMap("geeMap");
    }

    static {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getSerializationConfig().setCompactSerializationConfig(new CompactSerializationConfig().setSerializers(new FooSer(), new BarSer()));
        hz = HazelcastClient.newHazelcastClient(clientConfig);
        System.out.println("Hazelcast Client: " + hz);
    }

}
