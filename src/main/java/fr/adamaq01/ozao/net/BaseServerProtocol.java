package fr.adamaq01.ozao.net;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.protocol.ServerProtocol;

import java.util.Collection;
import java.util.List;

public class BaseServerProtocol extends ServerProtocol {

    public BaseServerProtocol() {
        super("base");
    }

    @Override
    public boolean verify(Connection connection, Buffer buffer) {
        return true;
    }

    @Override
    public boolean verify(Connection connection, Packet packet) {
        return true;
    }

    @Override
    public Collection<Buffer> cut(Connection connection, Buffer buffer) {
        return List.of(buffer);
    }

    @Override
    public Packet decode(Connection connection, Buffer buffer) {
        return Packet.create(buffer);
    }

    @Override
    public Buffer encode(Connection connection, Packet packet) {
        return packet.getPayload();
    }
}
