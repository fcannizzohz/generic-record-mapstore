package com.hazelcast.fcannizzohz.mapstoredemo;

/**
 * Base message type carrying an identifier and a typed payload.
 * Having a genric base class forces users to provide a serializer because of generic type erasure at
 * runtime makes payload an Object requiring explicit serialization.
 *
 * @param <T> the payload type
 */
public abstract class Message<T> {

    private final int id;
    private final T payload;

    /**
     * Creates a new message.
     *
     * @param id unique message identifier
     * @param payload the payload
     */
    public Message(int id, T payload) {
        this.id = id;
        this.payload = payload;
    }

    /**
     * Returns the message id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the payload.
     */
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

    @Override
    public String toString() {
        return "Message{" + "id=" + id + ", payload=" + payload + '}';
    }
}
