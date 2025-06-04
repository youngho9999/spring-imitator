package live.listener;

import live.handler.DispatcherServlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

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

                System.out.println("---------------------------------------------");
                System.out.println("받은 데이터: " + message);
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
