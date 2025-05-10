package live.handler;

public class HttpMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpMessage(String message) {
        this.message = message;
    }

    public HttpMessage() {
    }
}
