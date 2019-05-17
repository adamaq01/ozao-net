package fr.adamaq01.ozao.net.client.backend.udp;

import fr.adamaq01.ozao.net.Buffer;
import fr.adamaq01.ozao.net.OzaoException;
import fr.adamaq01.ozao.net.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;

class UDPChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private UDPClient client;

    protected UDPChannelHandler(UDPClient client) {
        this.client = client;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.client.getHandlers().forEach(handler -> handler.onConnect(client));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.client.getHandlers().forEach(handler -> handler.onDisconnect(client));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        Buffer buffer = Buffer.create(ReferenceCountUtil.retain(msg.content()));
        if (!this.client.getProtocol().verify(buffer))
            throw new OzaoException("Received a packet that does not suit the protocol requirements !");
        Packet packet = this.client.getProtocol().decode(buffer);
        this.client.getHandlers().forEach(handler -> handler.onPacketReceive(client, packet));
        this.client.getPacketHandlers().stream().filter(packetHandler -> packetHandler.verify(packet)).forEach(handler -> handler.onPacketReceive(client, packet));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.client.getHandlers().forEach(handler -> handler.onException(client, cause));
    }
}
