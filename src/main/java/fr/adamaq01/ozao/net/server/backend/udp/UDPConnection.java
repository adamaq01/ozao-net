package fr.adamaq01.ozao.net.server.backend.udp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.packet.PacketContainer;
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
    protected void sendPacket0(Packet packet) {
        channel.writeAndFlush(server.getProtocol().encode(packet).getData());
    }

    @Override
    protected void sendPackets0(PacketContainer packetContainer) {
        packetContainer.forEach(packet -> channel.write(server.getProtocol().encode(packet).getData()));
        channel.flush();
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }
}
