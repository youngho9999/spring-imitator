package live.listener;

import live.handler.DispatcherServlet;
import live.handler.HttpMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Worker implements Runnable {
    private final SocketChannel channel;
    private final DispatcherServlet dispatcherServlet;
    private final Poller poller;

    public Worker(SocketChannel channel, DispatcherServlet dispatcherServlet, Poller poller) {
        this.channel = channel;
        this.dispatcherServlet = dispatcherServlet;
        this.poller = poller;
    }

    @Override
    public void run() {

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        try {
            int read = channel.read(buffer);
            if(read == -1) {
                channel.close();
            } else {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                String message = new String(data);

                String[] splitted = message.split(" ");
                HttpMessage request = new HttpMessage(splitted[0], splitted[1]);
                String response = dispatcherServlet.dispatch(request);
                response += "\n";

                ByteBuffer responseBuffer = ByteBuffer.wrap(response.getBytes(StandardCharsets.UTF_8));
                buffer.clear();
                channel.write(responseBuffer);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        recoverReadOP();
    }

    private void recoverReadOP() {
        PollerEvent opRead = new PollerEvent(PollerEvent.Type.OP_READ_REGISTER, channel);
        poller.getEventQueue().offer(opRead);

        if(poller.getCount().incrementAndGet() ==0) {
            poller.getSelector().wakeup();
        }
    }
}
