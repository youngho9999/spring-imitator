package live.listener;

import java.nio.channels.SocketChannel;

public class PollerEvent {

    private final Type type;
    private final SocketChannel channel;

    public PollerEvent(Type type, SocketChannel channel) {
        this.type = type;
        this.channel = channel;
    }

    public Type getType() {
        return type;
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public enum Type {
        SOCK_REGISTER, OP_READ_REGISTER
    }
}
