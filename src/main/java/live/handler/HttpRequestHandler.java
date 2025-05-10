package live.handler;

public interface HttpRequestHandler {

    void handleRequest(HttpMessage input, HttpMessage output);
}

