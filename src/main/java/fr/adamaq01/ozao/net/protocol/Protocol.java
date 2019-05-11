package fr.adamaq01.ozao.net.protocol;

import fr.adamaq01.ozao.net.Buffer;
import fr.adamaq01.ozao.net.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public abstract class Protocol {

    protected String identifier;
    protected Map<String, Object> defaultValues;

    protected Protocol(String identifier, Map<String, Object>... defaultValues) {
        this.identifier = identifier;
        this.defaultValues = defaultValues != null && defaultValues.length > 0 ? defaultValues[0] : new HashMap<>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract boolean verify(Buffer buffer);

    public abstract boolean verify(Packet packet);

    public abstract Packet decode(Buffer buffer);

    public abstract Buffer encode(Packet packet);

    protected Packet fillDefaultValues(Packet packet) {
        defaultValues.keySet().stream().filter(s -> !packet.contains(s)).forEach(s -> packet.put(s, defaultValues.get(s)));

        return packet;
    }
}
