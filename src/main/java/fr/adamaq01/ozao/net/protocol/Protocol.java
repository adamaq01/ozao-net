package fr.adamaq01.ozao.net.protocol;

import fr.adamaq01.ozao.net.Buffer;
import fr.adamaq01.ozao.net.packet.Packet;

public abstract class Protocol {

    protected String identifier;

    protected Protocol(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract Packet decode(Buffer data);

    public abstract Buffer encode(Packet packet);
}
