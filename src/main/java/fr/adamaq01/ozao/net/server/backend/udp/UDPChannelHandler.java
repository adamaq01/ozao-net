package fr.adamaq01.ozao.net.server.backend.udp;

import fr.adamaq01.ozao.net.Buffer;
import fr.adamaq01.ozao.net.OzaoException;
import fr.adamaq01.ozao.net.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

class UDPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private UDPServer server;
    private UDPConnection connection;

    protected UDPChannelHandler(UDPServer server, UDPConnection connection) {
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.server.getHandlers().forEach(handler -> handler.onConnect(server, connection));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        server.getModifiableConnections().remove(connection);
        this.server.getHandlers().forEach(handler -> handler.onDisconnect(server, connection));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Buffer buffer = Buffer.create(ReferenceCountUtil.retain(msg));
        if (!this.server.getProtocol().verify(buffer))
            throw new OzaoException("Received a packet that does not suit the protocol requirements !");
        Packet packet = this.server.getProtocol().decode(buffer);
        this.server.getHandlers().forEach(handler -> handler.onPacketReceive(server, connection, packet));
        this.server.getPacketHandlers().stream().filter(packetHandler -> packetHandler.verify(packet)).forEach(handler -> handler.onPacketReceive(server, connection, packet));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        this.server.getHandlers().forEach(handler -> handler.onException(server, connection, cause));
    }
}
