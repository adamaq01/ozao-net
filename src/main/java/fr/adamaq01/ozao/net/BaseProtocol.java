package fr.adamaq01.ozao.net;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

public class BaseProtocol extends Protocol {

    public BaseProtocol() {
        super("base");
    }

    @Override
    public boolean verify(Buffer buffer) {
        return true;
    }

    @Override
    public boolean verify(Packet packet) {
        return true;
    }

    @Override
    public Packet decode(Buffer buffer) {
        return Packet.create(buffer);
    }

    @Override
    public Buffer encode(Packet packet) {
        return packet.getPayload();
    }
}
