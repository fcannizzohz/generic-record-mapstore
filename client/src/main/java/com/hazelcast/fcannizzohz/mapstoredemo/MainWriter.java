package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.HazelcastJsonValue;

import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.barMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.fooMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.geeMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.plopMap;

/**
 * Demo client that writes sample entries to the demo maps.
 */
public class MainWriter {
    /**
     * Populates the server with a few sample entries.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Plop plop = new Plop(2, System.currentTimeMillis());
        plopMap().put("plop1Key", plop);
        System.out.println("written plop1Key: " + plop);
        Foo foo = new Foo(3, "foo1Value");
        fooMap().put("foo1Key", foo);
        System.out.println("written foo1Key: " + foo);
        Bar bar = new Bar(4, 500L);
        barMap().put("bar1Key", bar);
        System.out.println("written bar1Key: " + bar);
        HazelcastJsonValue gee = new HazelcastJsonValue(new Gee("6", "gee1Value").toJson());
        geeMap().put("gee1Key", gee);
        System.out.println("written gee1Key: " + gee);
    }

}