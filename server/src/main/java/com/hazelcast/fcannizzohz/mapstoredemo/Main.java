package com.hazelcast.fcannizzohz.mapstoredemo;

import com.esotericsoftware.kryo.Kryo;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.NativeMemoryConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        config.setLicenseKey(System.getenv("HZ_LICENSEKEY"));
        setConfigFor(config, "fooMap");
        setConfigFor(config, "barMap");
        setConfigFor(config, "plopMap");

        config.getMapConfig("geeMap")
              .setMapStoreConfig(new MapStoreConfig()
                .setWriteDelaySeconds(0)
                .setImplementation(new JsonValueMapStore())
        );
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        System.out.println("started " + hz);
    }

    private static void setConfigFor(Config config, String map) {
        config.getMapConfig(map)
              .setBackupCount(1)
              .setTimeToLiveSeconds(3600)
              .setMapStoreConfig(new MapStoreConfig()
                      .setWriteDelaySeconds(0)
                      .setFactoryImplementation(new GenericRecordMapStoreFactory())
              );
    }
}