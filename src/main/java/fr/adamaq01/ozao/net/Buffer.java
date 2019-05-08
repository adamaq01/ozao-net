package fr.adamaq01.ozao.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

public final class Buffer {

    private static final Unsafe UNSAFE;

    static {
        Unsafe unsafe;
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            unsafe = null;
        }
        UNSAFE = unsafe;
    }

    public static Buffer create() {
        return new Buffer(Unpooled.buffer());
    }

    public static Buffer create(ByteBuf data) {
        return new Buffer(data);
    }

    public static <T extends NetSerializable> Buffer from(T object) {
        return object.write(new Buffer(Unpooled.buffer()));
    }

    private ByteBuf data;

    private Buffer(ByteBuf data) {
        this.data = data;
    }

    public ByteBuf getData() {
        return data;
    }

    public Buffer putBuffer(Buffer src) {
        this.data.writeBytes(src.getData());

        return this;
    }

    public Buffer putBytes(byte... src) {
        this.data.writeBytes(src);

        return this;
    }

    public byte[] getBytes(int length) {
        byte[] data = new byte[length];
        this.data.readBytes(data);

        return data;
    }

    public byte[] getAllBytes() {
        byte[] data = new byte[length()];
        this.data.getBytes(0, data);

        return data;
    }

    public Buffer putByte(byte b) {
        this.data.writeByte(b);

        return this;
    }

    public byte getByte() {
        return this.data.readByte();
    }

    public Buffer putBoolean(boolean b) {
        this.data.writeBoolean(b);

        return this;
    }

    public boolean getBoolean() {
        return this.data.readBoolean();
    }

    public Buffer putShort(short s, ByteOrder... order) {
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

    public Buffer putInt(int i, ByteOrder... order) {
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

    public Buffer putLong(long l, ByteOrder... order) {
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

    public Buffer putFloat(float f, ByteOrder... order) {
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

    public Buffer putDouble(double b, ByteOrder... order) {
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

    public Buffer putChar(char c, ByteOrder... order) {
        this.data.writeChar(order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN) ? Character.reverseBytes(c) : c);

        return this;
    }

    public char getChar(ByteOrder... order) {
        if (order != null && order.length != 0 && order[0].equals(ByteOrder.LITTLE_ENDIAN))
            return Character.reverseBytes(this.data.readChar());
        else
            return this.data.readChar();
    }

    public Buffer putString(String s, ByteOrder... order) {
        putShort((short) s.getBytes().length, order);
        this.data.writeCharSequence(s, Charset.forName("UTF-8"));

        return this;
    }

    public String getString(ByteOrder... order) {
        short length = getShort(order);
        return this.data.readCharSequence(length, Charset.forName("UTF-8")).toString();
    }

    public Buffer readerIndex(int index) {
        this.data.readerIndex(index);

        return this;
    }

    public int readerIndex() {
        return this.data.readerIndex();
    }

    public int length() {
        return this.data.readableBytes() + this.data.readerIndex();
    }

    public int readableBytes() {
        return this.data.readableBytes();
    }

    public Buffer slice(int start, int... end) {
        return new Buffer(this.data.slice(start, end != null && end.length != 0 && end[0] >= 0 && end[0] <= length() - start ? end[0] : length() - start));
    }

    public Buffer sliceCopy(int start, int... end) {
        return new Buffer(this.data.copy(start, end != null && end.length != 0 && end[0] >= 0 && end[0] <= length() - start ? end[0] : length() - start));
    }

    public Buffer copy() {
        return new Buffer(this.data.copy());
    }

    public Buffer clear() {
        this.data.clear();

        return this;
    }

    public <T extends NetSerializable> T to(Class<T> clazz) {
        try {
            Optional<Constructor<?>> constructor = Arrays.stream(clazz.getDeclaredConstructors()).filter(c -> c.getParameterCount() == 0).findFirst();
            T object;
            if (constructor.isPresent() && constructor.get().trySetAccessible())
                object = (T) constructor.get().newInstance();
            else
                object = (T) UNSAFE.allocateInstance(clazz);
            object.read(this);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
