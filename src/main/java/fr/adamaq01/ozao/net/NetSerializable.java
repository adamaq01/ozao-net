package fr.adamaq01.ozao.net;

import fr.adamaq01.ozao.net.packet.Packet;

public interface NetSerializable<T> {

    public Packet write(Packet packet);

    public T read(Packet packet);
}
