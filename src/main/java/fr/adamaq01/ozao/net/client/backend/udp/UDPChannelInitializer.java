package fr.adamaq01.ozao.net.client.backend.udp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.DatagramChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

class UDPChannelInitializer extends ChannelInitializer<DatagramChannel> {

    private UDPClient client;

    protected UDPChannelInitializer(UDPClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(DatagramChannel ch) {
        client.channel = ch;
        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(client.getTimeout()));
        ch.pipeline().addLast(new UDPChannelHandler(client));
    }
}
