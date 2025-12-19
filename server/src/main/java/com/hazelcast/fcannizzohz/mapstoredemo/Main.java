package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Entry point that starts a Hazelcast member configured with map-stores for different demo maps.
 */
public class Main {
    /**
     * Bootstraps a new Hazelcast member and configures map-stores for the demo maps.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Config config = new Config();
        config.setLicenseKey(System.getenv("HZ_LICENSEKEY"));
        setConfigFor(config, "fooMap");
        setConfigFor(config, "barMap");
        setConfigFor(config, "plopMap");

        config.getMapConfig("geeMap").setMapStoreConfig(new MapStoreConfig().setWriteDelaySeconds(0).setImplementation(new JsonValueMapStore()));
        HazelcastInstance hz = Hazelcast.newHazelcastInstance(config);
        System.out.println("started " + hz);
    }

    /**
     * Applies a common map configuration including synchronous map-store writes using {@link GenericRecordMapStoreFactory}.
     */
    private static void setConfigFor(Config config, String map) {
        config.getMapConfig(map).setBackupCount(1).setTimeToLiveSeconds(3600).setMapStoreConfig(new MapStoreConfig().setWriteDelaySeconds(0).setFactoryImplementation(new GenericRecordMapStoreFactory()));
    }
}