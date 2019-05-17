# OzaoNet (Java Networking Library)

##### OzaoNet is a Java Networking Library based on [Netty](https://netty.io) which provides multiple ways of sending and receiving packets, it is mainly inspired by [SimpleNet](https://github.com/jhg023/SimpleNet) and [Kryonet](https://github.com/EsotericSoftware/kryonet).

****

#### Add the dependency to your project
![Release](https://jitpack.io/v/Adamaq01/ozao-net.svg)

```Gradle
repositories {
    maven { url "https://jitpack.io" }
}
```

```Gradle
dependencies {
    compile 'com.github.Adamaq01:ozao-net:2.2.0'
}
```

****

## Example

Here is a simple example of a UDP server and client that should produce this output.

```
[Server] Mike has arrived, he carries this message: Do you want some chocolate ?
[Client] Pike has arrived, he carries this message: Yes please, I love chocolate !
[Client] Received disconnect message 42
```

The two first packets that carries Bird objects will be compressed with Zstandard basic's functions (no dictionnary) and the last one will not be compressed.

##### Bird Class (which implements NetSerializable):

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
    public Buffer write(Buffer packet) {
        return packet.putString(name).putString(message);
    }

    @Override
    public Bird read(Buffer packet) {
        this.name = packet.getString();
        this.message = packet.getString();

        return this;
    }
}
```

##### UDP Server:
```Java
// Instantiate a new UDP server, set timeout delay, add a basic handler then bind the server
Server server = new UDPServer(new OzaoProtocol()).setTimeout(15).addHandler(new ServerHandlerAdapater() {

    @Override
    public void onPacketReceive(Server server, Connection connection, Packet packet) {
        // Read the packet payload into a Bird (See OzaoProtocol for keys specifications)
        Bird bird = packet.<Buffer>get("payload").to(Bird.class);
        System.out.println(String.format("[Server] %s has arrived, he carries this message: %s", bird.getName(), bird.getMessage()));
        // Respond with a compressed packet containing a Bird and its informations
        connection.sendPacket(Packet.create().put("compression", true).put("payload", Buffer.from(new Bird("Pike", "Yes please, I love chocolate !"))));
        // Send a new packet (not compressed) which contains a simple byte to tell the client to disconnect
        connection.sendPacket(Packet.create().put("compression", false).put("payload", Buffer.create().putByte((byte) 42)));
        // Close the server
        server.close();
    }
}).bind(2812);
```

##### UDP Client:
```Java
// Instantiate a new UDP client, add a basic handler then connect to the server
Client client = new UDPClient(new OzaoProtocol()).addHandler(new ClientHandlerAdapter() {

    @Override
    public void onConnect(Client client) {
        // Send a compressed packet which contains a Bird and its informations when the client gets connected to the server
        Bird bird = new Bird("Mike", "Do you want some chocolate ?");
        client.sendPacket(Packet.create().put("compression", true).put("payload", Buffer.from(bird)));
    }

    @Override
    public void onPacketReceive(Client client, Packet packet) {
        // Create a new variable that contains the packet's payload
        Buffer payload = packet.get("payload");
        if (payload.readableBytes() == 1) { // If there is only one byte then it's the packet that tells us to disconnect
            System.out.println("[Client] Received disconnect message " + payload.getByte());
            // Disconnect from the server and stop the client
            client.disconnect();
        } else {
            // Read the packet payload into a Bird
            Bird bird = payload.to(Bird.class);
            System.out.println(String.format("[Client] %s has arrived, he carries this message: %s", bird.getName(), bird.getMessage()));
        }
    }
}).connect("localhost", 2812);
```

****

#### Credits:
###### - Shevchik's [UdpServerSocketChannel](https://github.com/Shevchik/UdpServerSocketChannel)
###### - [Netty](https://netty.io)
###### - luben's [zstd-jni](https://github.com/luben/zstd-jni)
###### - [Log4J2](https://logging.apache.org/log4j/2.x/)
