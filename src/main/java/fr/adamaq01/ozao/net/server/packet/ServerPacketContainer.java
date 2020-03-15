package fr.adamaq01.ozao.net.server.packet;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.Connection;
import fr.adamaq01.ozao.net.server.protocol.ServerProtocol;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class ServerPacketContainer {

    private Collection<Packet> packets;

    private ServerPacketContainer(Packet... packets) {
        this.packets = Arrays.asList(packets);
    }

    public static ServerPacketContainer create(Packet... packets) {
        return new ServerPacketContainer(packets);
    }

    public boolean verify(Connection connection, ServerProtocol protocol) {
        return packets.stream().allMatch(packet -> protocol.verify(connection, packet));
    }

    public ServerPacketContainer filter(Connection connection, ServerProtocol protocol) {
        packets.removeIf(packet -> !protocol.verify(connection, packet));

        return this;
    }

    public ServerPacketContainer put(Packet... packets) {
        this.packets.addAll(Arrays.asList(packets));

        return this;
    }

    public void forEach(Consumer<Packet> packetConsumer) {
        this.packets.forEach(packetConsumer);
    }
}
