package fr.adamaq01.ozao.net.packet;

import fr.adamaq01.ozao.net.protocol.Protocol;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class PacketContainer {

    public static PacketContainer create(Packet... packets) {
        return new PacketContainer(packets);
    }

    private Collection<Packet> packets;

    private PacketContainer(Packet... packets) {
        this.packets = Arrays.asList(packets);
    }

    public boolean verify(Protocol protocol) {
        return packets.stream().allMatch(packet -> protocol.verify(packet));
    }

    public PacketContainer filter(Protocol protocol) {
        packets.removeIf(packet -> !protocol.verify(packet));

        return this;
    }

    public PacketContainer put(Packet... packets) {
        Arrays.stream(packets).forEachOrdered(packet -> this.packets.add(packet));

        return this;
    }

    public void forEach(Consumer<Packet> packetConsumer) {
        this.packets.forEach(packetConsumer);
    }
}
