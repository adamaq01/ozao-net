package fr.adamaq01.ozao.net.client;

import fr.adamaq01.ozao.net.packet.Packet;

public interface ClientHandler {

    void onConnect(Client client);

    void onDisconnect(Client client);

    void onPacketReceive(Client client, Packet packet);

    void onException(Client client, Throwable cause);
}
