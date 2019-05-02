package fr.adamaq01.ozao.net.server;

import java.util.List;

public abstract class ServerBackend {

    protected ServerBackend() {
    }

    protected abstract void bind(int port);

    protected abstract void close();

    protected abstract List<Connection> getConnections();

    protected abstract List<ServerHandler> getHandlers();
}
