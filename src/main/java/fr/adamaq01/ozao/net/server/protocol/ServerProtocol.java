package fr.adamaq01.ozao.net.server.protocol;

import fr.adamaq01.ozao.net.Buffer;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Adamaq01 (Adam THIBERT)
 */
public abstract class ServerProtocol {

    protected String identifier;
    protected Map<String, Object> defaultValues;

    protected ServerProtocol(String identifier, Map<String, Object>... defaultValues) {
        this.identifier = identifier;
        this.defaultValues = defaultValues != null && defaultValues.length > 0 ? defaultValues[0] : new HashMap<>();
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract boolean verify(Connection connection, Buffer buffer);

    public abstract boolean verify(Connection connection, Packet packet);

    public abstract Collection<Buffer> cut(Connection connection, Buffer buffer);

    public abstract Packet decode(Connection connection, Buffer buffer);

    public abstract Buffer encode(Connection connection, Packet packet);

    protected Packet fillDefaultValues(Packet packet) {
        defaultValues.keySet().stream().filter(s -> !packet.contains(s)).forEach(s -> packet.put(s, defaultValues.get(s)));

        return packet;
    }
}
