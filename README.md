# Generic Record MapStore Demo

A minimal, two‑module Maven project that demonstrates how to persist Hazelcast map entries using `MapStore` in two different ways:

- As `GenericRecord` (Compact serialization) with a pluggable byte serializer (`Serde`), persisted into an in‑memory backing store (for illustration purposes, but it can be any data store).
- As `HazelcastJsonValue` (raw JSON), persisted as UTF‑8 bytes.

The project consists of a server that hosts Hazelcast and a simple in‑memory "DB", and a client that writes and reads entries from several maps.

Having the project split in two modules allows clients to use domain objects that aren't in the server classpath.
This means that it's guaranteed that Compact serialized object end up in memory as `GenericRecord`.

## Modules

- `server`: Starts a Hazelcast member and configures MapStore for maps. Contains the in‑memory `Db`, `MapStore` implementations, and `Serde`s.
- `client`: Starts a Hazelcast client and provides writer/reader examples that interact with the server’s maps.

## Key Features

- `MapStore` with GenericRecord payloads using a pluggable serializer (`KryoSerde` by default).
- `MapStore` with JSON payloads via `HazelcastJsonValue`.
- Compact serializers on the client for domain types (`Foo`, `Bar`).
- Simple in‑memory persistence simulated by `Db` (a `ConcurrentHashMap`).

## Maps in this demo

- `fooMap` → values of type `Foo` (Compact). Stored as `GenericRecord` on the server.
- `barMap` → values of type `Bar` (Compact). Stored as `GenericRecord` on the server.
- `plopMap` → values of type `Plop` (record). Stored as `GenericRecord` on the server.
- `geeMap` → values of type `HazelcastJsonValue` that wrap JSON produced from `Gee`. Stored as UTF‑8 bytes on the server.

All `MapStore` write delays are set to `0` (write‑through) so writes go to the backing store synchronously.

## Requirements

- Hazelcast Enterprise license key to run the server (set `HZ_LICENSEKEY` in your environment)

## Quick Start (recommended: run from IDE)

1. Export your Hazelcast Enterprise license key for the server:
   - macOS/Linux: `export HZ_LICENSEKEY=YOUR_KEY`
   - Windows (PowerShell): `$Env:HZ_LICENSEKEY='YOUR_KEY'`
2. Start the server:
   - Run `com.hazelcast.fcannizzohz.mapstoredemo.Main` from the `server` module.
3. Start a client reader (periodic reads):
    - Run `com.hazelcast.fcannizzohz.mapstoredemo.MainReader` from the `client` module.
4Start a client writer (one‑shot writes):
   - Run `com.hazelcast.fcannizzohz.mapstoredemo.MainWriter` from the `client` module.

You should see logs on the server like "storing <key>: <GenericRecord/JsonValue>" and on the client like "written ..." and periodic "reading ..." messages.

> Note: The backing store here is an in‑memory `ConcurrentHashMap` (`Db`) and is for demonstration only. Replace it with a real database for production.

## Building with Maven

This project is a multi‑module Maven build (root `pom.xml` with modules `server` and `client`).

- Build all modules: `mvn -DskipTests package`
- Build just the server: `mvn -pl server -am -DskipTests package`
- Build just the client: `mvn -pl client -am -DskipTests package`

Running from the command line requires constructing the runtime classpath for modules plus dependencies. It’s simplest to run from an IDE. If you prefer CLI, consider adding the Maven Exec Plugin to the module POMs (see snippet in `docs/usage.md`).

## Architecture Overview

- The server configures four maps in `Main`:
  - `fooMap`, `barMap`, `plopMap` → `MapStore` created by `GenericRecordMapStoreFactory`, persisting bytes produced by `Serde` (`KryoSerde` by default).
  - `geeMap` → `JsonValueMapStore`, persisting raw JSON bytes from `HazelcastJsonValue`.
- The server‑side `Db` is a simplistic in‑memory store used by the `MapStore`s.
- The client configures Compact serializers for `Foo` and `Bar` so they can be serialized as GenericRecords.

See `docs/architecture.md` for a deeper dive, and `docs/usage.md` for step‑by‑step instructions and expected output.

## Extending the demo

- Swap `Serde` to a different implementation (e.g., use Hazelcast’s `SerializationService` by toggling the factory code).
- Add new maps and types; register Compact serializers on the client and let the server persist via `GenericRecordMapStore`.
- Replace `Db` with a real database layer (JDBC, JPA, etc.).
