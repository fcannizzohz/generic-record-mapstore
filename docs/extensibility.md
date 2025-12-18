# Extensibility

This demo is intentionally small, but it’s designed to be easy to extend. Below are common extension points and how to implement them.

## Add a new map with GenericRecord persistence

1. Define your domain class on the client and (optionally) a Compact serializer.
   - If you use simple Java records or POJOs, Hazelcast can derive a schema automatically; explicit serializers give you full control.
2. Register your serializer in the client’s `Utils` (CompactSerializationConfig):
   ```java
   clientConfig.getSerializationConfig()
               .setCompactSerializationConfig(new CompactSerializationConfig()
                   .setSerializers(new FooSer(), new BarSer(), new MyTypeSer())
               );
   ```
3. On the server, configure the map in `Main.setConfigFor(config, "myMap")` (or add a new method like it):
   ```java
   setConfigFor(config, "myMap");
   ```
   This wires `GenericRecordMapStore` through `GenericRecordMapStoreFactory` with write‑through semantics.

## Switch the byte format (`Serde`)

`GenericRecordMapStore` relies on the `Serde` interface to convert `GenericRecord` ↔ `byte[]`.

- Default: `KryoSerde`.
- Alternative: `SerializationServiceSerde` (uses Hazelcast internal `SerializationService`).

To switch globally, change the factory in `GenericRecordMapStoreFactory` to return:

```java
return new GenericRecordMapStore(Db.getInstance(), new SerializationServiceSerde(hz));
```

(See the commented code for an example using a bootstrapped instance that implements `SerializationServiceSupport`.)

## Persist to a real database

Replace the simple `Db` with your persistence layer:

1. Define a repository interface that abstracts `store`, `load`, `delete`, `loadAll`, `keySet`.
2. Implement it with your database technology (JDBC, JPA/Hibernate, R2DBC, etc.). Consider batching for `storeAll`/`deleteAll`.
3. Inject your repository into the MapStore (via the factory) instead of the in‑memory `Db`.
4. Migrate bytes format as needed:
   - Keep `Serde` if you want to store compact `GenericRecord` bytes.
   - Or map `GenericRecord` fields to relational columns/JSON documents.

## Customize MapStore behavior

- Change `writeDelaySeconds` to enable write‑behind (buffered, async writes).
- Tune `backupCount`, `timeToLiveSeconds`, and other `MapConfig` properties.
- Implement `MapLoaderLifecycleSupport` hooks (already implemented) to manage external resources.

## Add a JSON‑backed map

- Follow the `geeMap` example:
  - Use `HazelcastJsonValue` on the client.
  - On the server, configure `JsonValueMapStore` for the map.
  - Ensure consistent UTF‑8 encoding for both storing and loading.

