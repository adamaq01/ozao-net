package fr.adamaq01.ozao.net.server.backend.tcp;

import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.ServerBackend;
import fr.adamaq01.ozao.net.server.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TCPServerBackend extends ServerBackend {

    protected ChannelFuture channelFuture;
    protected List<Connection> connections;
    protected List<ServerHandler> handlers;

    public TCPServerBackend() {
        this.connections = new ArrayList<>();
        this.handlers = new ArrayList<>();
    }

    @Override
    protected void bind(int port) {
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
    }

    @Override
    protected void close() {
        try {
            channelFuture.channel().disconnect().sync().channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected List<Connection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    @Override
    protected List<ServerHandler> getHandlers() {
        return handlers;
    }
}
