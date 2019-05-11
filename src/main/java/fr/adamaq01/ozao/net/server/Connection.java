package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.OzaoException;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

import java.net.InetSocketAddress;

public abstract class Connection {

    protected Server server;

    protected Connection(Server server) {
        this.server = server;
    }

    public Connection sendPacket(Packet packet) {
        if (!this.server.protocol.verify(packet))
            this.server.handlers.forEach(handler -> handler.onException(server, this, new OzaoException("Tried to send a packet that does not suit the protocol requirements !")));
        else
            sendPacket0(packet);

        return this;
    }

    protected abstract void sendPacket0(Packet packet);

    public abstract InetSocketAddress getAddress();
}
