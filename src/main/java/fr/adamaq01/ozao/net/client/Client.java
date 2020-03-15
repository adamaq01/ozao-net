package fr.adamaq01.ozao.net.client;

import fr.adamaq01.ozao.net.OzaoException;
import fr.adamaq01.ozao.net.client.packet.ClientPacketContainer;
import fr.adamaq01.ozao.net.client.protocol.ClientProtocol;
import fr.adamaq01.ozao.net.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public abstract class Client {

    protected ClientProtocol protocol;
    protected int timeout;
    protected List<ClientHandler> handlers;
    protected List<ClientPacketHandler> packetHandlers;

    protected Client(ClientProtocol protocol) {
        this.protocol = protocol;
        this.timeout = 30;
        this.handlers = new ArrayList<>();
        this.packetHandlers = new ArrayList<>();
    }

    public abstract Client connect(String address, int port);

    public abstract Client disconnect();

    public Client setTimeout(int timeout) {
        this.timeout = timeout;

        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public ClientProtocol getProtocol() {
        return protocol;
    }

    public Client addHandler(ClientHandler handler) {
        this.handlers.add(handler);

        return this;
    }

    public Client addPacketHandler(ClientPacketHandler packetHandler) {
        this.packetHandlers.add(packetHandler);

        return this;
    }

    public List<ClientHandler> getHandlers() {
        return handlers;
    }

    public List<ClientPacketHandler> getPacketHandlers() {
        return packetHandlers;
    }

    public final Client sendPackets(ClientPacketContainer packetContainer) {
        if (!packetContainer.verify(this.protocol)) {
            this.handlers.forEach(handler -> handler.onException(this, new OzaoException("Tried to send one or more packets that does not suit the protocol requirements ! These packets will not be sent !")));
            packetContainer.filter(this.protocol);
        }
        this.sendPackets0(packetContainer);

        return this;
    }

    public final Client sendPacket(Packet packet) {
        if (!this.protocol.verify(packet))
            this.handlers.forEach(handler -> handler.onException(this, new OzaoException("Tried to send a packet that does not suit the protocol requirements !")));
        else
            sendPacket0(packet);

        return this;
    }

    protected abstract void sendPacket0(Packet packet);

    protected abstract void sendPackets0(ClientPacketContainer packetContainer);
}
