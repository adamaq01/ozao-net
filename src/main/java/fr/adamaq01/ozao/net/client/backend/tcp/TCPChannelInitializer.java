package fr.adamaq01.ozao.net.client.backend.tcp;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

class TCPChannelInitializer extends ChannelInitializer<SocketChannel> {

    private TCPClientBackend backend;

    protected TCPChannelInitializer(TCPClientBackend backend) {
        this.backend = backend;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        backend.channel = ch;
        ch.pipeline().addLast(new TCPChannelHandler(backend));
    }
}
