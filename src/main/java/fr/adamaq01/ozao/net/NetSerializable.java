package fr.adamaq01.ozao.net;

public interface NetSerializable<T> {

    Buffer write(Buffer packet);

    T read(Buffer packet);
}
