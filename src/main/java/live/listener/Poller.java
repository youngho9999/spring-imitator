package live.listener;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Poller implements Runnable {


    private final Queue<SocketChannel> eventQueue = new ConcurrentLinkedQueue<>();
    private final Selector selector;
    private final AtomicInteger count = new AtomicInteger(-1);

    public Poller() throws IOException {
        this.selector = Selector.open();
    }

    @Override
    public void run() {

        int selected = 0;

        while(true) {

            selected = 0;

            while(!eventQueue.isEmpty()) {
                registerSocket();
            }

            try {
                if(count.getAndSet(-1) >= 0) {
                    selected = selector.selectNow();
                } else {
                    selected = selector.select();
                    count.incrementAndGet();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(selected == 0) {
                continue;
            }

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if(key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
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

                            System.out.println("받은 데이터: " + message);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }

    }

    private void registerSocket() {
        SocketChannel socket = eventQueue.poll();
        try {
            socket.configureBlocking(false);
            socket.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Queue<SocketChannel> getEventQueue() {
        return eventQueue;
    }

    public Selector getSelector() {
        return selector;
    }

    public AtomicInteger getCount() {
        return count;
    }
}
