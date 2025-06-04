package live;

import live.context.ComponentScanner;
import live.context.SpringContext;
import live.context.SpringContextInitiator;
import live.handler.DispatcherServlet;
import live.handler.PathContextInitiator;
import live.listener.Acceptor;
import live.listener.Poller;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        initializeSpringContext();
        intializeDispatcherServlet();
        initializeWebServer();
    }

    private static void initializeWebServer() {
        final int maxThreads = 2;
        DispatcherServlet dispatcherServlet = (DispatcherServlet) SpringContext.BEAN_MAP.get(DispatcherServlet.class);
        ExecutorService executorService = Executors.newFixedThreadPool(maxThreads);

        try {
            Poller poller = new Poller(executorService, dispatcherServlet);
            Acceptor acceptor = new Acceptor(poller);

            new Thread(poller).start();
            new Thread(acceptor).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeSpringContext() {
        // 1. 컴포넌트 스캔
        Set<Class<?>> scannedComponents = ComponentScanner.scanAllComponents();

        // 2. 컨텍스트 초기화 및 의존성 주입
        SpringContextInitiator initiator = new SpringContextInitiator();
        initiator.init(scannedComponents);
    }

    private static void intializeDispatcherServlet() {
        PathContextInitiator initiator = new PathContextInitiator();
        initiator.scanHandlers();
    }
}