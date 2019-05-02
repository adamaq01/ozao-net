# OzaoNet (Java Networking Library)

##### OzaoNet is a Java Networking Library based on [Netty](https://netty.io) which provides multiple ways of sending and receiving packets, it is mainly inspired by [SimpleNet](https://github.com/jhg023/SimpleNet) and [Kryonet](https://github.com/EsotericSoftware/kryonet)

****

#### Add the dependency to your project
![Release](https://jitpack.io/v/Adamaq01/ozao-net.svg)

```Gradle
repositories {
    maven { url "https://jitpack.io" }
}
```
```
dependencies {
    compile 'com.github.Adamaq01:ozao-net:1.0.0'
}
```

****

## Examples
##### UDP Server:
```Java
Server server = Server.create(new UDPServerBackend());
server.addHandler(new ServerHandler() {
    @Override
    public void onPacketReceive(Connection connection, Packet packet) {
        System.out.println("Received a packet !");
        connection.sendPacket(Packet.create().putInt(0));
        server.close();
    }
});
server.bind(2812);
```

##### UDP Client:
```Java
Client client = Client.create(new UDPClientBackend());
client.addHandler(new ClientHandler() {
    @Override
    public void onPacketReceive(Packet packet) {
        System.out.println("Received a packet !");
        client.disconnect();
    }
});
client.connect("localhost", 2812);
```

****

#### Credits:
###### - Shevchik's [UdpServerSocketChannel](https://github.com/Shevchik/UdpServerSocketChannel)
