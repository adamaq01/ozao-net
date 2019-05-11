package fr.adamaq01.ozao.net;

import com.github.luben.zstd.Zstd;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

import java.util.Map;
import java.util.function.Function;

public class OzaoProtocol extends Protocol {

    private Function<Buffer, Buffer> compressFunction;
    private Function<Buffer, Buffer> decompressFunction;

    public OzaoProtocol() {
        this(buffer -> {
            byte[] data = buffer.getBytes(buffer.readableBytes());
            byte[] compressedData = Zstd.compress(data);
            buffer.getData().clear().writeInt(data.length).writeBytes(compressedData);
            return buffer;
        }, buffer -> {
            int length = buffer.getInt();
            byte[] data = buffer.getBytes(buffer.readableBytes());
            byte[] uncompressedData = Zstd.decompress(data, length);
            buffer.getData().clear().writeBytes(uncompressedData);
            return buffer;
        });
    }

    public OzaoProtocol(Function<Buffer, Buffer> compressFunction, Function<Buffer, Buffer> decompressFunction) {
        super("ozao", Map.of("compression", false));
        this.compressFunction = compressFunction;
        this.decompressFunction = decompressFunction;
    }

    @Override
    public boolean verify(Buffer buffer) {
        byte compression = buffer.getByte();
        buffer.readerIndex(0);
        return compression == 0 || compression == 1;
    }

    @Override
    public boolean verify(Packet packet) {
        fillDefaultValues(packet);
        return true;
    }

    @Override
    public Packet decode(Buffer buffer) {
        boolean compression = buffer.getBoolean();
        Buffer packetPayload = compression ? decompressFunction.apply(buffer.sliceCopy(1)) : buffer.sliceCopy(1);
        return Packet.create(packetPayload).put("compression", compression);
    }

    @Override
    public Buffer encode(Packet packet) {
        Buffer buffer = Buffer.create();
        boolean compression = packet.get("compression");
        buffer.putBoolean(compression);
        buffer.putBuffer(compression ? compressFunction.apply(packet.getPayload()) : packet.getPayload());
        return buffer;
    }
}
