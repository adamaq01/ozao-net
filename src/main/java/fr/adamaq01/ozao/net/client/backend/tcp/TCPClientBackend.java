package fr.adamaq01.ozao.net.client.backend.tcp;

import fr.adamaq01.ozao.net.client.ClientBackend;
import fr.adamaq01.ozao.net.client.ClientHandler;
import fr.adamaq01.ozao.net.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class TCPClientBackend extends ClientBackend {

    protected ChannelFuture channelFuture;
    protected SocketChannel channel;
    protected List<ClientHandler> handlers;

    public TCPClientBackend() {
        this.handlers = new ArrayList<>();
    }

    @Override
    protected void connect(InetSocketAddress address) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TCPChannelInitializer(this))
                    .option(ChannelOption.AUTO_READ, true);
            channelFuture = bootstrap.connect(address).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Client started

        new Thread(() -> {
            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                group.shutdownGracefully();
            }
        }).start();
    }

    @Override
    protected void disconnect() {
        try {
            channelFuture.channel().disconnect().sync().channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<ClientHandler> getHandlers() {
        return handlers;
    }

    @Override
    protected void sendPacket(Packet packet) {
        channel.writeAndFlush(packet.getData());
    }
}
