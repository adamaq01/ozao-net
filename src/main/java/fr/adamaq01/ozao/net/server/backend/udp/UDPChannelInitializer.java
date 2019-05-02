package fr.adamaq01.ozao.net.server.backend.udp;

import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.ReadTimeoutHandler;

class UDPChannelInitializer extends ChannelInitializer<UDPChannel> {

    private UDPServerBackend backend;

    protected UDPChannelInitializer(UDPServerBackend backend) {
        this.backend = backend;
    }

    @Override
    protected void initChannel(UDPChannel ch) throws Exception {
        UDPConnection connection = new UDPConnection(ch);
        ch.pipeline().addLast(new ReadTimeoutHandler(10));
        ch.pipeline().addLast(new UDPChannelHandler(backend, connection));
        backend.connections.add(connection);
    }
}
