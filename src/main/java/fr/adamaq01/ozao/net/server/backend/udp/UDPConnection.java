package fr.adamaq01.ozao.net.server.backend.udp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.Server;
import fr.adamaq01.ozao.net.server.packet.ServerPacketContainer;

import java.net.InetSocketAddress;

class UDPConnection extends Connection {

    protected UDPChannel channel;

    protected UDPConnection(Server server, UDPChannel channel) {
        super(server);
        this.channel = channel;
    }

    @Override
    protected void sendPacket0(Packet packet) {
        channel.writeAndFlush(server.getProtocol().encode(this, packet).getData());
    }

    @Override
    protected void sendPackets0(ServerPacketContainer packetContainer) {
        packetContainer.forEach(packet -> channel.write(server.getProtocol().encode(this, packet).getData()));
        channel.flush();
    }

    @Override
    public InetSocketAddress getAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    @Override
    public void close() {
        channel.close();
    }
}
