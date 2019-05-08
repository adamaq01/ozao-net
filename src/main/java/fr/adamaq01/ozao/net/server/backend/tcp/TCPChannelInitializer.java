package fr.adamaq01.ozao.net.server.backend.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

class TCPChannelInitializer extends ChannelInitializer<SocketChannel> {

    private TCPServer server;

    protected TCPChannelInitializer(TCPServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        TCPConnection connection = new TCPConnection(server.getProtocol(), ch);
        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(server.getTimeout()));
        ch.pipeline().addLast(new TCPChannelHandler(server, connection));
        server.getModifiableConnections().add(connection);
    }
}
