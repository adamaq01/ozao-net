package fr.adamaq01.ozao.net.client;

import fr.adamaq01.ozao.net.packet.Packet;

import java.net.InetSocketAddress;
import java.util.List;

public abstract class ClientBackend {

    protected ClientBackend() {
    }

    protected abstract void connect(InetSocketAddress address);

    protected abstract void disconnect();

    protected abstract List<ClientHandler> getHandlers();

    protected abstract void sendPacket(Packet packet);
}
