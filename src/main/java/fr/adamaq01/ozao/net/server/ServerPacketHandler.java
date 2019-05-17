package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.packet.Packet;

import java.util.function.Function;

public abstract class ServerPacketHandler {

    protected Function<Packet, Boolean> verifyConsumer;

    public ServerPacketHandler(Function<Packet, Boolean> verifyConsumer) {
        this.verifyConsumer = verifyConsumer;
    }

    public abstract void onPacketReceive(Server server, Connection connection, Packet packet);

    public boolean verify(Packet packet) {
        return verifyConsumer.apply(packet);
    }
}
