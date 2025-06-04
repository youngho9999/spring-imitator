package live.listener;

import live.handler.DispatcherServlet;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class Poller implements Runnable {


    private final Queue<PollerEvent> eventQueue = new ConcurrentLinkedQueue<>();
    private final Selector selector;
    private final ExecutorService executorService;
    private final DispatcherServlet dispatcherServlet;
    private final AtomicInteger count = new AtomicInteger(-1);

    public Poller(ExecutorService executorService, DispatcherServlet dispatcherServlet) throws IOException {
        this.selector = Selector.open();
        this.executorService = executorService;
        this.dispatcherServlet = dispatcherServlet;
    }

    @Override
    public void run() {

        int selected = 0;
        boolean hasEvents = false;

        while(true) {

            hasEvents = false;
            selected = 0;

            while(!eventQueue.isEmpty()) {
                hasEvents = true;
                handleEvents();
            }

            try {
                if(hasEvents) {
                    selected = selector.selectNow();
                } else {
                    count.set(-1);
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
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);

                    Worker worker = new Worker(channel, dispatcherServlet,this);
                    executorService.execute(worker);
                }
            }
        }

    }

    private void handleEvents() {
        PollerEvent event = eventQueue.poll();
        SocketChannel socket = event.getChannel();

        if(event.getType() == PollerEvent.Type.SOCK_REGISTER) {
            try {
                socket.configureBlocking(false);
                socket.register(selector, SelectionKey.OP_READ);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (event.getType() == PollerEvent.Type.OP_READ_REGISTER) {

            final SelectionKey sk = socket.keyFor(getSelector());
            sk.interestOps(sk.interestOps() | SelectionKey.OP_READ);
        }
    }

    public Queue<PollerEvent> getEventQueue() {
        return eventQueue;
    }

    public Selector getSelector() {
        return selector;
    }

    public AtomicInteger getCount() {
        return count;
    }
}
