package fr.adamaq01.ozao.net.client.backend.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

class TCPChannelInitializer extends ChannelInitializer<SocketChannel> {

    private TCPClient client;

    protected TCPChannelInitializer(TCPClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        client.channel = ch;
        ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(client.getTimeout()));
        ch.pipeline().addLast(new TCPChannelHandler(client));
    }
}
