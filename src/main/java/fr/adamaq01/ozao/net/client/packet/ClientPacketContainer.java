package fr.adamaq01.ozao.net.client.packet;

import fr.adamaq01.ozao.net.client.protocol.ClientProtocol;
import fr.adamaq01.ozao.net.packet.Packet;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

public class ClientPacketContainer {

    private ClientPacketContainer(Packet... packets) {
        this.packets = Arrays.asList(packets);
    }

    private Collection<Packet> packets;

    public static ClientPacketContainer create(Packet... packets) {
        return new ClientPacketContainer(packets);
    }

    public boolean verify(ClientProtocol protocol) {
        return packets.stream().allMatch(protocol::verify);
    }

    public ClientPacketContainer filter(ClientProtocol protocol) {
        packets.removeIf(packet -> !protocol.verify(packet));

        return this;
    }

    public ClientPacketContainer put(Packet... packets) {
        this.packets.addAll(Arrays.asList(packets));

        return this;
    }

    public void forEach(Consumer<Packet> packetConsumer) {
        this.packets.forEach(packetConsumer);
    }
}
