# Usage

This guide shows how to run the demo, what to expect, and how to run it from Maven if you prefer the command line over an IDE.

## Prerequisites

- JDK 25
- Maven 3.9+
- Hazelcast Enterprise license key (set `HZ_LICENSEKEY` for the server)

## Running from an IDE (recommended)

1. Import the Maven project (it’s a multi‑module build with `server` and `client`).
2. Set the environment variable `HZ_LICENSEKEY` for the `server` run configuration.
3. Run the following classes:
   - Server: `server` → `com.hazelcast.fcannizzohz.mapstoredemo.Main`
   - Writer: `client` → `com.hazelcast.fcannizzohz.mapstoredemo.MainWriter`
   - Reader: `client` → `com.hazelcast.fcannizzohz.mapstoredemo.MainReader`

You can start the reader before or after the writer; the reader prints keys and values periodically.

## Expected Output

- On the server console:
  - A startup message: `started HazelcastInstance{name=...}`
  - For each write: messages like `storing foo1Key: ...` or `storing gee1Key: ...`
- On the writer console:
  - Messages like `written foo1Key: Message{id=3, payload=foo1Value}` (and similarly for other maps)
- On the reader console (repeats every 3 seconds):
  - Keys of each map and values for the specific demo keys:
    - `reading foo1Key: Message{id=3, payload=foo1Value}`
    - `reading bar1Key: Message{id=4, payload=500}`
    - `reading plop1Key: Plop[id=2, ts=... ]`
    - `reading gee1Key: {"id":"6","name":"gee1Value"}` (as `HazelcastJsonValue`)

## Maps and Types

- `fooMap`: values are `Foo` (Compact) → persisted as `GenericRecord` via `GenericRecordMapStore`.
- `barMap`: values are `Bar` (Compact) → persisted as `GenericRecord` via `GenericRecordMapStore`.
- `plopMap`: values are `Plop` (record) → persisted as `GenericRecord` via `GenericRecordMapStore`.
- `geeMap`: values are `HazelcastJsonValue` wrapping JSON from `Gee` → persisted by `JsonValueMapStore`.

## Troubleshooting

- Missing license key: the server will fail to start; set `HZ_LICENSEKEY` in your environment before starting the server.
- Client can’t connect: ensure the server is running and reachable. The client uses default Hazelcast client config (localhost by default).
- No values returned by the reader: ensure you ran the writer at least once after server start; the in‑memory backing store does not survive restarts.
- Charset inconsistency warning: `JsonValueMapStore.loadAll` uses default platform charset in one conversion path; for production, consider standardizing on UTF‑8 everywhere.
