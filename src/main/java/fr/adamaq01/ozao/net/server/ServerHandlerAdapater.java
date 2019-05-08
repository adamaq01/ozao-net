package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.packet.Packet;

public class ServerHandlerAdapater implements ServerHandler {

    @Override
    public void onConnect(Server server, Connection connection) {
    }

    @Override
    public void onDisconnect(Server server, Connection connection) {
    }

    @Override
    public void onPacketReceive(Server server, Connection connection, Packet packet) {
    }

    @Override
    public void onException(Server server, Connection connection, Throwable cause) {
    }
}
