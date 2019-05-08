package fr.adamaq01.ozao.net.packet;

import java.util.HashMap;

public final class Packet {

    public static Packet create() {
        return new Packet();
    }

    private HashMap<String, Object> data;

    private Packet() {
        this.data = new HashMap<>();
    }

    public <T> T get(String key) {
        return (T) this.data.get(key);
    }

    public <T> Packet put(String key, T value) {
        this.data.put(key, value);

        return this;
    }
}
