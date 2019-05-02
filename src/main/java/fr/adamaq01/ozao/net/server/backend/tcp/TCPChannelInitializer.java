package fr.adamaq01.ozao.net.server.backend.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

class TCPChannelInitializer extends ChannelInitializer<SocketChannel> {

    private TCPServerBackend backend;

    protected TCPChannelInitializer(TCPServerBackend backend) {
        this.backend = backend;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        TCPConnection connection = new TCPConnection(ch);
        ch.pipeline().addLast(new TCPChannelHandler(backend, connection));
        backend.connections.add(connection);
    }
}
