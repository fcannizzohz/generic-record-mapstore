package com.hazelcast.fcannizzohz.mapstoredemo;

/**
 * Simple event-like DTO represented as a Java record.
 * Proves that DTOs of this type don't need explicit compact serializers.
 *
 * @param id identifier
 * @param ts event timestamp (epoch millis)
 */
public record Plop(int id, long ts) {
}
