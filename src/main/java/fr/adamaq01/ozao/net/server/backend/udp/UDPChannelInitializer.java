package fr.adamaq01.ozao.net.server.backend.udp;

import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.ReadTimeoutHandler;

class UDPChannelInitializer extends ChannelInitializer<UDPChannel> {

    private UDPServer server;

    protected UDPChannelInitializer(UDPServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(UDPChannel ch) {
        UDPConnection connection = new UDPConnection(server.getProtocol(), ch);
        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(server.getTimeout()));
        ch.pipeline().addLast(new UDPChannelHandler(server, connection));
        server.getModifiableConnections().add(connection);
    }
}
