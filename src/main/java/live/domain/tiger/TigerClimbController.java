package live.domain.tiger;

import live.context.Component;
import live.handler.Controller;
import live.handler.HttpMessage;
import live.handler.HttpRequestHandler;

@Component
@Controller("/climb")
public class TigerClimbController implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpMessage input, HttpMessage output) {
        String message = input.getBody() + " is Climbing";
        output.setBody(message);
    }
}
