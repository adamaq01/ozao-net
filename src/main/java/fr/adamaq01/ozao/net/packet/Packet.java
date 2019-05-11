package fr.adamaq01.ozao.net.packet;

import fr.adamaq01.ozao.net.Buffer;

import java.util.HashMap;

public final class Packet {

    public static Packet create() {
        return new Packet(Buffer.create());
    }

    public static Packet create(Buffer payload) {
        return new Packet(payload);
    }

    private Buffer payload;
    private HashMap<String, Object> header;

    private Packet(Buffer payload) {
        this.payload = payload;
        this.header = new HashMap<>();
    }

    public <T> T get(String key) {
        return (T) this.header.get(key);
    }

    public boolean contains(String key) {
        return this.header.containsKey(key);
    }

    public <T> Packet put(String key, T value) {
        this.header.put(key, value);

        return this;
    }

    public Packet remove(String key) {
        this.header.remove(key);

        return this;
    }

    public HashMap<String, Object> getHeader() {
        return header;
    }

    public Buffer getPayload() {
        return payload;
    }

    public Packet setPayload(Buffer payload) {
        this.payload = payload;

        return this;
    }
}
