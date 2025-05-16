package live.handler;

public class HttpMessage {

    private String path;
    private String body;

    public HttpMessage(String body) {
        this.body = body;
    }

    public HttpMessage() {
    }

    public HttpMessage(String path, String body) {
        this.path = path;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
