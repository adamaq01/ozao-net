package fr.adamaq01.ozao.net.server.backend.tcp;

import fr.adamaq01.ozao.net.protocol.Protocol;
import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.List;

public class TCPServer extends Server {

    protected ChannelFuture channelFuture;

    public TCPServer(Protocol protocol) {
        super(protocol);
    }

    @Override
    public Server bind(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TCPChannelInitializer(this))
                    .childOption(ChannelOption.AUTO_READ, true);
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
