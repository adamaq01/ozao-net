package fr.adamaq01.ozao.net.client;

import fr.adamaq01.ozao.net.packet.Packet;

public class ClientHandlerAdapter implements ClientHandler {

    @Override
    public void onConnect(Client client) {
    }

    @Override
    public void onDisconnect(Client client) {
    }

    @Override
    public void onPacketReceive(Client client, Packet packet) {
    }

    @Override
    public void onException(Client client, Throwable cause) {
    }
}
