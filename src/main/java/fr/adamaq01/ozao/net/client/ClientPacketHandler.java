package fr.adamaq01.ozao.net.client;

import fr.adamaq01.ozao.net.packet.Packet;

import java.util.function.Function;

public abstract class ClientPacketHandler {

    protected Function<Packet, Boolean> verifyConsumer;

    public ClientPacketHandler(Function<Packet, Boolean> verifyConsumer) {
        this.verifyConsumer = verifyConsumer;
    }

    public abstract void onPacketReceive(Client client, Packet packet);

    public boolean verify(Packet packet) {
        return verifyConsumer.apply(packet);
    }
}
