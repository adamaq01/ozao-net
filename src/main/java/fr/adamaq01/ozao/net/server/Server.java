package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.OzaoException;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.protocol.Protocol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Server {

    protected Protocol protocol;
    protected int timeout;
    protected List<Connection> connections;
    protected List<ServerHandler> handlers;
    protected List<ServerPacketHandler> packetHandlers;

    protected Server(Protocol protocol) {
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

    public Protocol getProtocol() {
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
        if (!this.protocol.verify(packet))
            this.handlers.forEach(handler -> handler.onException(this, null, new OzaoException("Tried to broadcast a packet that does not suit the protocol requirements !")));
        else
            getConnections().forEach(connection -> connection.sendPacket0(packet));

        return this;
    }

    public Server broadcastPacketExcept(Packet packet, Connection... connections) {
        if (!this.protocol.verify(packet))
            this.handlers.forEach(handler -> handler.onException(this, null, new OzaoException("Tried to broadcast a packet that does not suit the protocol requirements !")));
        else
            getConnections().stream().filter(connection -> !Arrays.stream(connections).anyMatch(connection1 -> connection1.equals(connection))).forEach(connection -> connection.sendPacket0(packet));

        return this;
    }
}
