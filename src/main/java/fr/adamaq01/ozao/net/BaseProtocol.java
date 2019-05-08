package fr.adamaq01.ozao.net;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

public class BaseProtocol extends Protocol {

    public BaseProtocol() {
        super("base");
    }

    @Override
    public Packet decode(Buffer rawBuffer) {
        return Packet.create().put("payload", rawBuffer);
    }

    @Override
    public Buffer encode(Packet packet) {
        return packet.get("payload");
    }
}
