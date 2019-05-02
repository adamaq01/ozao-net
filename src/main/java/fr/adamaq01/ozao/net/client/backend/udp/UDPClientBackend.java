package fr.adamaq01.ozao.net.client.backend.udp;

import fr.adamaq01.ozao.net.client.ClientBackend;
import fr.adamaq01.ozao.net.client.ClientHandler;
import fr.adamaq01.ozao.net.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class UDPClientBackend extends ClientBackend {

    protected ChannelFuture channelFuture;
    protected DatagramChannel channel;
    protected List<ClientHandler> handlers;

    public UDPClientBackend() {
        this.handlers = new ArrayList<>();
    }

    @Override
    protected void connect(InetSocketAddress address) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new UDPChannelInitializer(this))
                    .option(ChannelOption.AUTO_READ, true);
            channelFuture = bootstrap.connect(address).sync();
            // Client started
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
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
