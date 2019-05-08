package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

import java.net.InetSocketAddress;

public abstract class Connection {

    protected Protocol protocol;

    protected Connection(Protocol protocol) {
        this.protocol = protocol;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public abstract Connection sendPacket(Packet packet);

    public abstract InetSocketAddress getAddress();
}
