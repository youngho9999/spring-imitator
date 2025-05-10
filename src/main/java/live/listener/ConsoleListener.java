package live.listener;

import live.context.AutoWire;
import live.context.Component;
import live.handler.DispatcherServlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class ConsoleListener {

    private final DispatcherServlet dispatcherServlet;

    @AutoWire
    public ConsoleListener(DispatcherServlet dispatcherServlet) {
        this.dispatcherServlet = dispatcherServlet;
    }

    public void listenConsole() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input;

        try {
            while ((input = reader.readLine()) != null) { // 입력이 더 이상 없을 때 null 반환
                if (input.equalsIgnoreCase("exit")) {
                    break; // "exit" 입력 시 종료
                }
                String[] splitInput = input.split(" ");
                dispatcherServlet.dispatch(splitInput[0], splitInput[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
