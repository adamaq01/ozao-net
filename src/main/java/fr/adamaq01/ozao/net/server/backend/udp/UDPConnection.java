package fr.adamaq01.ozao.net.server.backend.udp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;
import fr.adamaq01.ozao.net.server.Connection;

import java.net.InetSocketAddress;

class UDPConnection extends Connection {

    protected UDPChannel channel;

    protected UDPConnection(Protocol protocol, UDPChannel channel) {
        super(protocol);
        this.channel = channel;
    }

    @Override
    public Connection sendPacket(Packet packet) {
        channel.writeAndFlush(protocol.encode(packet).getData());

        return this;
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }
}

