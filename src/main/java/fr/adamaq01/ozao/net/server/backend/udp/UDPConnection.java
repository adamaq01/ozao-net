package fr.adamaq01.ozao.net.server.backend.udp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.Server;

import java.net.InetSocketAddress;

class UDPConnection extends Connection {

    protected UDPChannel channel;

    protected UDPConnection(Server server, UDPChannel channel) {
        super(server);
        this.channel = channel;
    }

    @Override
    public void sendPacket0(Packet packet) {
        channel.writeAndFlush(server.getProtocol().encode(packet).getData());
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }
}

