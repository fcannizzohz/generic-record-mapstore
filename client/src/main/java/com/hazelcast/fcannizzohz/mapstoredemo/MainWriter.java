package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.map.IMap;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.barMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.fooMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.plopMap;

public class MainWriter {
    public static void main(String[] args)
            throws InterruptedException {
        plopMap().put("plop1Key", new Plop(1, System.currentTimeMillis()));
        System.out.println("written plop1Key");
        fooMap().put("foo1Key", new Foo(1, "foo1Value"));
        System.out.println("written foo1Key");
        barMap().put("bar1Key", new Bar(2, 200L));
        System.out.println("written bar1Key");
    }

}