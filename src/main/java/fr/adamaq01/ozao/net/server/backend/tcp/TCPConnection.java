package fr.adamaq01.ozao.net.server.backend.tcp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.Server;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

class TCPConnection extends Connection {

    private SocketChannel channel;

    protected TCPConnection(Server server, SocketChannel channel) {
        super(server);
        this.channel = channel;
    }

    @Override
    public void sendPacket0(Packet packet) {
        channel.writeAndFlush(server.getProtocol().encode(packet).getData());
    }

    @Override
    public InetSocketAddress getAddress() {
        return channel.remoteAddress();
    }
}

