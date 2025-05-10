package live.domain.tiger;

import live.handler.Controller;
import live.handler.HttpRequestHandler;

@Controller("/climb")
public class TigerClimbController implements HttpRequestHandler {
    @Override
    public void handleRequest(String path, String body) {
        System.out.println(body + " is Climbing");
    }
}
