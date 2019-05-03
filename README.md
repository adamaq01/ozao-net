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
    compile 'com.github.Adamaq01:ozao-net:1.0.1'
}
```

****

## Examples

##### Net Serializable Class:
```Java
public class Bird implements NetSerializable<Bird> {
    
    private String name;
    private String message;
    
    public Bird(String name, String message) {
        this.name = name;
        this.message = message;
    }
    
    public String getName() {
        return name;
    }
    
    public String getMessage() {
        return message;
    }
    
    @Override
    public Packet write(Packet packet) {
        return packet.putString(name).putString(message);
    }
    
    @Override
    public Bird read(Packet packet) {
        this.name = packet.getString();
        this.message = packet.getString();
        
        return this;
    }
}
```

##### UDP Server:
```Java
Server server = Server.create(new UDPServerBackend());
server.addHandler(new ServerHandler() {

    @Override
    public void onPacketReceive(Connection connection, Packet packet) {
        Bird bird = new Bird("", "").read(packet);
        System.out.println(String.format("%s has arrived, he carries this message: %s", bird.getName(), bird.getMessage()));
        connection.sendPacket(new Bird("Pike", "Yes please, I love chocolate !").write(Packet.create()));
        connection.sendPacket(Packet.create().putByte((byte) 42));
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
    public void onConnect() {
        client.sendPacket(new Bird("Mike", "Do you want some chocolate ?").write(Packet.create()));
    }

    @Override
    public void onPacketReceive(Packet packet) {
        if (packet.readableBytes() == 1)
            client.disconnect();
        else {
            Bird bird = new Bird("", "").read(packet);
            System.out.println(String.format("%s has arrived, he carries this message: %s", bird.getName(), bird.getMessage()));
        }
    }
});
client.connect("localhost", 2812);
```

****

#### Credits:
###### - Shevchik's [UdpServerSocketChannel](https://github.com/Shevchik/UdpServerSocketChannel)
