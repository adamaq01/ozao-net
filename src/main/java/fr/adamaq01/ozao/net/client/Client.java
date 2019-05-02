package fr.adamaq01.ozao.net.client;

import fr.adamaq01.ozao.net.packet.Packet;

import java.net.InetSocketAddress;
import java.util.List;

public class Client {

    public static Client create(ClientBackend backend) {
        return new Client(backend);
    }

    private ClientBackend backend;

    private Client(ClientBackend backend) {
        this.backend = backend;
    }

    public void connect(String address, int port) {
        this.connect(new InetSocketAddress(address, port));
    }

    public void connect(InetSocketAddress address) {
        this.backend.connect(address);
    }

    public void disconnect() {
        this.backend.disconnect();
    }

    public List<ClientHandler> getHandlers() {
        return this.backend.getHandlers();
    }

    public Client addHandler(ClientHandler handler) {
        this.backend.getHandlers().add(handler);

        return this;
    }

    public Client sendPacket(Packet packet) {
        this.backend.sendPacket(packet);

        return this;
    }
}
