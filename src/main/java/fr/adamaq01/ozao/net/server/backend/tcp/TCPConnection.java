package fr.adamaq01.ozao.net.server.backend.tcp;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;
import io.netty.channel.socket.SocketChannel;

import java.net.InetSocketAddress;

class TCPConnection implements Connection {

    private SocketChannel channel;

    protected TCPConnection(SocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet.getData());
    }

    @Override
    public InetSocketAddress getAddress() {
        return channel.remoteAddress();
    }
}

