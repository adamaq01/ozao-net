package fr.adamaq01.ozao.net.client.backend.tcp;

import fr.adamaq01.ozao.net.client.Client;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.packet.PacketContainer;
import fr.adamaq01.ozao.net.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class TCPClient extends Client {

    protected ChannelFuture channelFuture;
    protected SocketChannel channel;

    public TCPClient(Protocol protocol) {
        super(protocol);
    }

    @Override
    public Client connect(InetSocketAddress address) {
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

        return this;
    }

    @Override
    public Client disconnect() {
        try {
            channelFuture.channel().disconnect().sync().channel().close().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return this;
    }

    @Override
    protected void sendPacket0(Packet packet) {
        channel.writeAndFlush(protocol.encode(packet).getData());
    }

    @Override
    protected void sendPackets0(PacketContainer packetContainer) {
        packetContainer.forEach(packet -> channel.write(protocol.encode(packet).getData()));
        channel.flush();
    }
}
