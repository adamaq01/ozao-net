package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.OzaoException;
import fr.adamaq01.ozao.net.client.Client;
import fr.adamaq01.ozao.net.packet.Packet;
import fr.adamaq01.ozao.net.packet.PacketContainer;
import fr.adamaq01.ozao.net.protocol.Protocol;

import java.net.InetSocketAddress;

public abstract class Connection {

    protected Server server;

    protected Connection(Server server) {
        this.server = server;
    }

    public final Connection sendPackets(PacketContainer packetContainer) {
        if (!packetContainer.verify(this.server.protocol)) {
            this.server.handlers.forEach(handler -> handler.onException(server, this, new OzaoException("Tried to send one or more packets that does not suit the protocol requirements ! These packets will not be sent !")));
            packetContainer.filter(this.server.protocol);
        }
        this.sendPackets0(packetContainer);

        return this;
    }

    public final Connection sendPacket(Packet packet) {
        if (!this.server.protocol.verify(packet))
            this.server.handlers.forEach(handler -> handler.onException(server, this, new OzaoException("Tried to send a packet that does not suit the protocol requirements !")));
        else
            sendPacket0(packet);

        return this;
    }

    protected abstract void sendPacket0(Packet packet);

    protected abstract void sendPackets0(PacketContainer packetContainer);

    public abstract InetSocketAddress getAddress();
}
