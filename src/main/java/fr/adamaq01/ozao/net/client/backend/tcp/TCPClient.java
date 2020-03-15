package fr.adamaq01.ozao.net.client.backend.tcp;

import fr.adamaq01.ozao.net.client.Client;
import fr.adamaq01.ozao.net.client.packet.ClientPacketContainer;
import fr.adamaq01.ozao.net.client.protocol.ClientProtocol;
import fr.adamaq01.ozao.net.packet.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TCPClient extends Client {

    protected ChannelFuture channelFuture;
    protected SocketChannel channel;

    public TCPClient(ClientProtocol protocol) {
        super(protocol);
    }

    @Override
    public Client connect(String address, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new TCPChannelInitializer(this))
                    .option(ChannelOption.AUTO_READ, true);
            channelFuture = bootstrap.connect(address, port).sync();
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
    protected void sendPackets0(ClientPacketContainer packetContainer) {
        packetContainer.forEach(packet -> channel.write(protocol.encode(packet).getData()));
        channel.flush();
    }
}
