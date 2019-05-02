package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.packet.Packet;

import java.util.Arrays;
import java.util.List;

public class Server {

    public static Server create(ServerBackend backend) {
        return new Server(backend);
    }

    private ServerBackend backend;

    private Server(ServerBackend backend) {
        this.backend = backend;
    }

    public void bind(int port) {
        this.backend.bind(port);
    }

    public void close() {
        this.backend.close();
    }

    public int getNumConnected() {
        return this.backend.getConnections().size();
    }

    public List<Connection> getConnections() {
        return this.backend.getConnections();
    }

    public List<ServerHandler> getHandlers() {
        return this.backend.getHandlers();
    }

    public Server addHandler(ServerHandler handler) {
        this.backend.getHandlers().add(handler);

        return this;
    }

    public Server broadcastPacket(Packet packet) {
        getConnections().forEach(connection -> connection.sendPacket(packet));

        return this;
    }

    public Server broadcastPacketExcept(Packet packet, Connection... connections) {
        getConnections().stream().filter(connection -> !Arrays.stream(connections).anyMatch(connection1 -> connection1.equals(connection))).forEach(connection -> connection.sendPacket(packet));

        return this;
    }
}
