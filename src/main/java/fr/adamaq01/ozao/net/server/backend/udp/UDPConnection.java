package fr.adamaq01.ozao.net.server.backend.udp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;

import java.net.InetSocketAddress;

class UDPConnection implements Connection {

    protected UDPChannel channel;

    protected UDPConnection(UDPChannel channel) {
        this.channel = channel;
    }

    @Override
    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet.getData());
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }
}

