package live.domain.tiger;

import live.handler.Controller;
import live.handler.HttpMessage;
import live.handler.HttpRequestHandler;

@Controller("/climb")
public class TigerClimbController implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpMessage input, HttpMessage output) {
        String message = input + " is Climbing";
        output.setMessage(message);
    }
}
