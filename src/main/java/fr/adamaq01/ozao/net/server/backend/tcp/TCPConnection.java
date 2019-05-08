package fr.adamaq01.ozao.net.server.backend.tcp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;
import fr.adamaq01.ozao.net.server.Connection;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

class TCPConnection extends Connection {

    private SocketChannel channel;

    protected TCPConnection(Protocol protocol, SocketChannel channel) {
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
        return channel.remoteAddress();
    }
}

