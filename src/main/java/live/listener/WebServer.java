package live.listener;

import live.context.AutoWire;
import live.context.Component;
import live.handler.DispatcherServlet;
import live.handler.HttpMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class WebServer {

    private final DispatcherServlet dispatcherServlet;
    private static final int PORT = 9999;

    @AutoWire
    public WebServer(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }

    public void listenSocket() {

        // try-with-resources 구문을 사용하여 ServerSocket이 자동으로 닫히도록 합니다.
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while(true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientRunner(clientSocket, dispatcherServlet)).start();
            }
        } catch (IOException e) {
            System.out.println("서버 소켓을 열 수 없습니다 (포트 " + PORT + " 사용 중일 수 있음): " + e.getMessage());
        }
        System.out.println("서버를 종료합니다.");
    }
}
