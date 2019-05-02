package fr.adamaq01.ozao.net.client.backend.udp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;

class UDPChannelInitializer extends ChannelInitializer<DatagramChannel> {

    private UDPClientBackend backend;

    protected UDPChannelInitializer(UDPClientBackend backend) {
        this.backend = backend;
    }

    @Override
    protected void initChannel(DatagramChannel ch) throws Exception {
        backend.channel = ch;
        ch.pipeline().addLast(new UDPChannelHandler(backend));
    }
}
