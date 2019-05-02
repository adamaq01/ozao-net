package fr.adamaq01.ozao.net.server;

import fr.adamaq01.ozao.net.packet.Packet;

import java.net.InetSocketAddress;

public interface Connection {

    public void sendPacket(Packet packet);

    public InetSocketAddress getAddress();
}
