package com.hazelcast.fcannizzohz.mapstoredemo;

public abstract class Message<T> {

    private int id;
    private T payload;
    public Message(int id, T payload) {
        this.id = id;
        this.payload = payload;
    }

    public int getId() {
        return id;
    }

    public T getPayload() {
        return payload;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return this.id == ((Message) obj).id && this.payload.equals(((Message) obj).payload);
    }

    public String toString() {
        return "Message{" + "id=" + id + ", payload=" + payload + '}';
    }
}
