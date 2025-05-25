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
            try (Socket clientSocket = serverSocket.accept();
                 InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
                 BufferedReader reader = new BufferedReader(inputStreamReader);
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String inputLine;

                while ((inputLine = reader.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(inputLine.trim())) {
                        writer.println("서버에서 연결을 종료합니다. 안녕히 가세요!");
                        System.out.println("클라이언트가 'exit'을 요청하여 연결을 종료합니다.");
                        break;
                    }

                    String[] splitInput = inputLine.split(" ");

                    HttpMessage request = new HttpMessage(splitInput[0], splitInput[1]);
                    String output = dispatcherServlet.dispatch(request);

                    writer.println(output);
                }

            } catch (IOException e) {
                System.out.println("클라이언트와의 통신 중 오류 발생: " + e.getMessage());
            } finally {
                System.out.println("클라이언트 연결이 종료되었습니다.");
            }

        } catch (IOException e) {
            System.out.println("서버 소켓을 열 수 없습니다 (포트 " + PORT + " 사용 중일 수 있음): " + e.getMessage());
        }
        System.out.println("서버를 종료합니다.");
    }
}
