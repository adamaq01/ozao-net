package fr.adamaq01.ozao.net.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class Packet {

    public static Packet create() {
        return new Packet(Unpooled.buffer());
    }

    public static Packet create(ByteBuf data) {
        return new Packet(data);
    }

    private ByteBuf data;

    private Packet(ByteBuf data) {
        this.data = data;
    }

    public ByteBuf getData() {
        return data;
    }

    public Packet putPacket(Packet src) {
        this.data.writeBytes(src.getData());

        return this;
    }


    public Packet putBytes(byte... src) {
        this.data.writeBytes(src);

        return this;
    }

    public byte[] getBytes(int length) {
        byte[] data = new byte[length];
        this.data.readBytes(data);

        return data;
    }

    public Packet putByte(byte b) {
        this.data.writeByte(b);

        return this;
    }

    public byte getByte() {
        return this.data.readByte();
    }

    public Packet putBoolean(boolean b) {
        this.data.writeBoolean(b);

        return this;
    }

    public boolean getBoolean() {
        return this.data.readBoolean();
    }

    public Packet putShort(short s, ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            this.data.writeShortLE(s);
        else
            this.data.writeShort(s);

        return this;
    }

    public short getShort(ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            return this.data.readShortLE();
        else
            return this.data.readShort();
    }

    public Packet putInt(int i, ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            this.data.writeIntLE(i);
        else
            this.data.writeInt(i);

        return this;
    }

    public int getInt(ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            return this.data.readIntLE();
        else
            return this.data.readInt();
    }

    public Packet putLong(long l, ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            this.data.writeLongLE(l);
        else
            this.data.writeLong(l);

        return this;
    }

    public long getLong(ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            return this.data.readLongLE();
        else
            return this.data.readLong();
    }

    public Packet putFloat(float f, ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            this.data.writeFloatLE(f);
        else
            this.data.writeFloat(f);

        return this;
    }

    public float getFloat(ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            return this.data.readFloatLE();
        else
            return this.data.readFloat();
    }

    public Packet putDouble(double b, ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            this.data.writeDoubleLE(b);
        else
            this.data.writeDouble(b);

        return this;
    }

    public double getDouble(ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            return this.data.readDoubleLE();
        else
            return this.data.readDouble();
    }

    public Packet putChar(char c, ByteOrder... order) {
        this.data.writeChar(order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN) ? Character.reverseBytes(c) : c);

        return this;
    }

    public char getChar(ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            return Character.reverseBytes(this.data.readChar());
        else
            return this.data.readChar();
    }

    public Packet putString(String s, ByteOrder... order) {
        putShort((short) s.getBytes().length, order);
        this.data.writeCharSequence(s, Charset.forName("UTF-8"));

        return this;
    }

    public String getString(ByteOrder... order) {
        short length = getShort(order);
        return this.data.readCharSequence(length, Charset.forName("UTF-8")).toString();
    }

    public Packet readerIndex(int index) {
        this.data.readerIndex(index);

        return this;
    }

    public int readerIndex() {
        return this.data.readerIndex();
    }

    public int length() {
        return this.data.capacity();
    }

    public int readableBytes() {
        return this.data.readableBytes();
    }

    public Packet slice(int start, int... end) {
        return new Packet(this.data.slice(start, end != null && end.length != 0 && end[0] >= 0 && end[0] <= length() - start ? end[0] : length() - start));
    }

    public Packet sliceCopy(int start, int... end) {
        return new Packet(this.data.copy(start, end != null && end.length != 0 && end[0] >= 0 && end[0] <= length() - start ? end[0] : length() - start));
    }
}
