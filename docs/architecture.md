# Architecture

This repository demonstrates two MapStore approaches on a Hazelcast cluster:

- GenericRecord persistence (Compact serialization) backed by a pluggable byte serializer (`Serde`).
- JSON persistence using `HazelcastJsonValue` stored as UTF‑8 bytes.

The code is intentionally compact to make the data flow and extension points obvious.

## High‑level Components

- Server module
  - Starts a Hazelcast member (`server/src/main/java/.../Main.java`).
  - Configures four IMaps:
    - `fooMap`, `barMap`, `plopMap` → persisted via `GenericRecordMapStoreFactory` → `GenericRecordMapStore`.
    - `geeMap` → persisted via `JsonValueMapStore`.
  - Provides a simple in‑memory backing store `Db` (a `ConcurrentHashMap<String, byte[]>`).
  - Provides `Serde` implementations to convert `GenericRecord` to/from bytes.

- Client module
  - Starts a Hazelcast client with Compact serializers registered for domain classes (`FooSer`, `BarSer`).
  - `MainWriter` writes one entry to each map.
  - `MainReader` periodically reads and prints values/keys from the maps.

## Data Flow

1. Client puts an object into a map (e.g., `Foo` into `fooMap`).
2. Client‑side Compact serialization converts `Foo`/`Bar` to Compact format; Hazelcast transmits it to the member.
3. On the server, map writes invoke `MapStore.store(key, value)`:
   - For `fooMap`, `barMap`, `plopMap`:
     - `GenericRecordMapStore` receives a `GenericRecord` representation of the value.
     - The active `Serde` converts the `GenericRecord` to bytes.
     - `Db.store(key, bytes)` writes bytes to the in‑memory store.
   - For `geeMap`:
     - `JsonValueMapStore` receives a `HazelcastJsonValue`.
     - The raw JSON string is converted to UTF‑8 bytes and written to `Db`.
4. On read/load operations (`MapStore.load`/`loadAll`), the inverse happens:
   - For GenericRecord: bytes → `Serde.fromBytes` → `GenericRecord` returned to Hazelcast.
   - For JSON: bytes → UTF‑8 string → `HazelcastJsonValue` returned to Hazelcast.

Because the backing `Db` is in‑memory, restarts do not retain data.

> NOTE: there's no knowledge of domain object in the server.

## Map Configuration (Server)

`server/src/main/java/.../Main.java`:

- `fooMap`, `barMap`, `plopMap` share the same MapStore settings:
  - `backupCount = 1`
  - `timeToLiveSeconds = 3600`
  - `MapStoreConfig.setWriteDelaySeconds(0)` (write‑through)
  - `MapStoreFactory`: `GenericRecordMapStoreFactory`
- `geeMap` uses `JsonValueMapStore` and `writeDelaySeconds(0)`.

## MapStore Implementations

### GenericRecordMapStore

- File: `server/src/main/java/.../GenericRecordMapStore.java`
- Persists `GenericRecord` values by converting them to/from bytes using a `Serde`.
- Construction path:
  - Through `GenericRecordMapStoreFactory`.
- Key methods:
  - `store(String key, GenericRecord value)` → serialize with `Serde.toBytes` → `Db.store`.
  - `load(String key)` → `Db.load` → `Serde.fromBytes`.
  - `storeAll`, `loadAll`, `delete`, `deleteAll`, `loadAllKeys`, `destroy` (delegation and helpers).

### JsonValueMapStore

- File: `server/src/main/java/.../JsonValueMapStore.java`
- Persists values of type `HazelcastJsonValue` by storing their JSON string as UTF‑8 bytes.

## Serde Strategies

The `Serde` interface abstracts conversion between `GenericRecord` and `byte[]`:

```java
public interface Serde {
    byte[] toBytes(GenericRecord obj);
    GenericRecord fromBytes(byte[] bytes);
}
```

Available implementations:

- `KryoSerde` (default) → uses Kryo to write/read class+object as bytes. Thread‑local `Kryo` instances, registration not required.
- `SerializationServiceSerde` (optional) → uses Hazelcast internal `SerializationService` to serialize `GenericRecord` to `Data` and back.

Selection is done in `GenericRecordMapStoreFactory`. The code currently returns `new GenericRecordMapStore(Db.getInstance(), new KryoSerde())`. Alternative (commented) code shows how to build with `SerializationServiceSerde` when a bootstrapped instance with `SerializationServiceSupport` is available.

## Backing Store (Db)

- File: `server/src/main/java/.../Db.java`
- In‑memory `ConcurrentHashMap<String, byte[]>` with methods: `store`, `load`, `delete`, `keySet`, `clear`.
- Used by both MapStore implementations.

## Client Serialization

- `client/src/main/java/.../Utils.java` sets up a `HazelcastClient` and registers Compact serializers via `CompactSerializationConfig.setSerializers(new FooSer(), new BarSer())`.
- Domain classes:
  - `Foo extends Message<String>`; `FooSer` reads/writes `id` and `payload`.
  - `Bar extends Message<Long>`; `BarSer` reads/writes `id` and `payload`.
  - `Plop` is a Java record `(int id, long ts)`; it’s used in `plopMap`, serialized as a GenericRecord by Hazelcast automatically.
  - `Gee` is a record `(String id, String name)`; it’s turned into JSON with Gson and wrapped in `HazelcastJsonValue` for `geeMap`.

## Error Handling & Assumptions

- `SerializationServiceSerde.fromBytes` validates that deserialized object is a `GenericRecord` and throws if not.
- `KryoSerde` similarly asserts the object type.
- `JsonValueMapStore.loadAll` converts bytes to `String` without specifying charset in one case (uses platform default in that lambda). Consider using `UTF_8` consistently if you harden this code.
- Backing store is non‑persistent.

## Extensibility Summary

- Add new domain types and Compact serializers (client) → they’ll arrive on server as `GenericRecord` and be persisted by `GenericRecordMapStore`.
- Swap/extend `Serde` to change byte format.
- Replace `Db` with a real database layer (JDBC driver, repository interface, batching, etc.).
