package live;

import live.context.ComponentScanner;
import live.context.SpringContext;
import live.context.SpringContextInitiator;
import live.listener.ConsoleListener;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        initializeSpringContext();
        intializeDispatcherServlet();


    }

    private static void initializeSpringContext() {
        // 1. 컴포넌트 스캔
        Set<Class<?>> scannedComponents = ComponentScanner.scanAllComponents();

        // 2. 컨텍스트 초기화 및 의존성 주입
        SpringContextInitiator initiator = new SpringContextInitiator();
        initiator.init(scannedComponents);
    }

    private static void intializeDispatcherServlet() {
        ConsoleListener consoleListener = (ConsoleListener) SpringContext.BEAN_MAP.get(ConsoleListener.class);
        consoleListener.listenConsole();
    }
}