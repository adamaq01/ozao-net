package fr.adamaq01.ozao.net;

import com.github.luben.zstd.Zstd;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

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
        super("ozao");
        this.compressFunction = compressFunction;
        this.decompressFunction = decompressFunction;
    }

    @Override
    public Packet decode(Buffer rawBuffer) {
        boolean compression = rawBuffer.getBoolean();
        Buffer packetPayload = compression ? decompressFunction.apply(rawBuffer.sliceCopy(1)) : rawBuffer.sliceCopy(1);
        return Packet.create().put("compression", compression).put("payload", packetPayload);
    }

    @Override
    public Buffer encode(Packet packet) {
        Buffer buffer = Buffer.create();
        boolean compression = packet.get("compression");
        buffer.putBoolean(compression);
        buffer.putBuffer(compression ? compressFunction.apply(packet.get("payload")) : packet.get("payload"));
        return buffer;
    }
}
