package live.listener;

import live.handler.DispatcherServlet;
import live.handler.HttpMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunner implements Runnable {

    private final Socket socket;
    private final DispatcherServlet dispatcherServlet;

    public ClientRunner(Socket socket, DispatcherServlet dispatcherServlet) {
        this.socket = socket;
        this.dispatcherServlet = dispatcherServlet;
    }

    @Override
    public void run() {

        try(InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

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
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
