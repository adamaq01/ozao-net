package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.packet.Packet;

public interface ServerHandler {

    void onConnect(Server server, Connection connection);

    void onDisconnect(Server server, Connection connection);

    void onPacketReceive(Server server, Connection connection, Packet packet);

    void onException(Server server, Connection connection, Throwable cause);
}
