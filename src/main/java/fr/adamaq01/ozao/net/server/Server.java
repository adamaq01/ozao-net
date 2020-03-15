package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.server.protocol.ServerProtocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Server {

    protected ServerProtocol protocol;
    protected int timeout;
    protected List<Connection> connections;
    protected List<ServerHandler> handlers;
    protected List<ServerPacketHandler> packetHandlers;

    protected Server(ServerProtocol protocol) {
        this.protocol = protocol;
        this.timeout = 30;
        this.connections = new ArrayList<>();
        this.handlers = new ArrayList<>();
        this.packetHandlers = new ArrayList<>();
    }

    public abstract Server bind(int port);

    public abstract boolean isBound();

    public abstract Server close();

    public abstract int getPort();

    public ServerProtocol getProtocol() {
        return protocol;
    }

    public Server setTimeout(int timeout) {
        this.timeout = timeout;

        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public List<Connection> getConnections() {
        return Collections.unmodifiableList(connections);
    }

    public Server addHandler(ServerHandler handler) {
        this.handlers.add(handler);

        return this;
    }

    public Server addPacketHandler(ServerPacketHandler packetHandler) {
        this.packetHandlers.add(packetHandler);

        return this;
    }

    public List<ServerHandler> getHandlers() {
        return handlers;
    }

    public List<ServerPacketHandler> getPacketHandlers() {
        return packetHandlers;
    }

    public Server broadcastPacket(Packet packet) {
        getConnections().forEach(connection -> connection.sendPacket(packet));

        return this;
    }

    public Server broadcastPacketExcept(Packet packet, Connection... connections) {
        getConnections().stream().filter(connection -> !Arrays.asList(connections).contains(connection)).forEach(connection -> connection.sendPacket(packet));

        return this;
    }
}
