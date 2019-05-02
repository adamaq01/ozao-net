package fr.adamaq01.ozao.net.client.backend.udp;

import fr.adamaq01.ozao.net.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

class UDPChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private UDPClientBackend clientBackend;

    protected UDPChannelHandler(UDPClientBackend clientBackend) {
        this.clientBackend = clientBackend;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        this.clientBackend.handlers.forEach(handler -> handler.onConnect());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        this.clientBackend.handlers.forEach(handler -> handler.onDisconnect());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Packet packet = Packet.create(ReferenceCountUtil.retain(msg));
        this.clientBackend.handlers.forEach(handler -> handler.onPacketReceive(packet));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
