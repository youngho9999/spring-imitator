package live.listener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Acceptor implements Runnable {

    private final Poller poller;

    public Acceptor(Poller poller) {
        this.poller = poller;
    }

    @Override
    public void run() {


        try(ServerSocketChannel server = ServerSocketChannel.open() ) {
            server.bind(new InetSocketAddress(9999));

            while(true) {
                SocketChannel client = server.accept();
                System.out.println("클라이언트 연결됨: " + client.getRemoteAddress());

                poller.getEventQueue().offer(client);
                if(poller.getCount().incrementAndGet() == 0) {
                    poller.getSelector().wakeup();
                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
