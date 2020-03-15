package fr.adamaq01.ozao.net.server.backend.udp;

import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.Server;
import fr.adamaq01.ozao.net.server.protocol.ServerProtocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.util.List;

public class UDPServer extends Server {

    protected ChannelFuture channelFuture;

    public UDPServer(ServerProtocol protocol) {
        super(protocol);
    }

    @Override
    public Server bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(UDPServerChannel.class)
                .childHandler(new UDPChannelInitializer(this))
                .childOption(ChannelOption.AUTO_READ, true);
        try {
            channelFuture = serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Server started

        new Thread(() -> {
            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }).start();

        return this;
    }

    @Override
    public boolean isBound() {
        return channelFuture.channel().isOpen();
    }

    @Override
    public Server close() {
        try {
            channelFuture.channel().disconnect().sync().channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public int getPort() {
        if (channelFuture == null || !isBound()) {
            return -1;
        } else {
            return ((InetSocketAddress) channelFuture.channel().localAddress()).getPort();
        }
    }

    protected List<Connection> getModifiableConnections() {
        return connections;
    }
}
