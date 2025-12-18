package com.hazelcast.fcannizzohz.mapstoredemo;

import com.hazelcast.core.HazelcastJsonValue;
import com.hazelcast.map.IMap;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.barMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.fooMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.geeMap;
import static com.hazelcast.fcannizzohz.mapstoredemo.Utils.plopMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainReader {
    public static void main(String[] args) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                IMap<String, Bar> barMap = barMap();
                IMap<String, Foo> fooMap = fooMap();
                IMap<String, Plop> plopMap = plopMap();
                IMap<String, HazelcastJsonValue> geeMap = geeMap();
                System.out.println("bar keys: " + barMap.keySet());
                System.out.println("foo keys: " + fooMap.keySet());
                System.out.println("plop keys: " + plopMap.keySet());
                Foo fooVal = fooMap.get("foo1Key");
                System.out.println("reading foo1Key: "  + fooVal);
                Bar barVal = barMap.get("bar1Key");
                System.out.println("reading foo2Key: "  + barVal);
                Plop plopVal = plopMap.get("plop1Key");
                System.out.println("reading plop1Key: "  + plopVal);
                HazelcastJsonValue json = geeMap.get("gee1Key");
                System.out.println("reading gee1Key: "  + json);
            }
        }, 0, 3, TimeUnit.SECONDS);
    }
}