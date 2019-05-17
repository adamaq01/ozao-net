package fr.adamaq01.ozao.net;

import com.github.luben.zstd.Zstd;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

import java.util.ArrayList;
import java.util.Collection;
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
        byte magic = buffer.getByte();
        byte compression = buffer.readerIndex(5).getByte();
        buffer.readerIndex(0);
        return magic == 42 && (compression == 0 || compression == 1);
    }

    @Override
    public boolean verify(Packet packet) {
        fillDefaultValues(packet);
        return true;
    }

    @Override
    public Collection<Buffer> cut(Buffer buffer) {
        ArrayList<Buffer> buffers = new ArrayList<>();
        int read = 0;
        while (read < buffer.length()) {
            int length = buffer.readerIndex(1).getInt();
            buffers.add(buffer.sliceCopy(read, length + 6));
            read += length + 6;
        }
        return buffers;
    }

    @Override
    public Packet decode(Buffer buffer) {
        boolean compression = buffer.readerIndex(5).getBoolean();
        Buffer packetPayload = compression ? decompressFunction.apply(buffer.sliceCopy(6)) : buffer.sliceCopy(6);
        return Packet.create(packetPayload).put("compression", compression);
    }

    @Override
    public Buffer encode(Packet packet) {
        boolean compression = packet.get("compression");
        Buffer buffer = Buffer.create();
        Buffer payload = compression ? compressFunction.apply(packet.getPayload()) : packet.getPayload();
        buffer.putByte((byte) 42);
        buffer.putInt(payload.length());
        buffer.putBoolean(compression);
        buffer.putBuffer(payload);
        return buffer;
    }
}
