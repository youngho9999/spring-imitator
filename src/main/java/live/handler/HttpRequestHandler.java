package live.handler;

public interface HttpRequestHandler {

    void handleRequest(String path, String body);
}

