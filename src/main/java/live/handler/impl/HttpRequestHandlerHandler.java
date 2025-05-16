package live.handler.impl;

import live.context.SpringContext;
import live.handler.Handler;
import live.handler.HttpMessage;
import live.handler.HttpRequestHandler;

import java.lang.reflect.Method;

public class HttpRequestHandlerHandler implements Handler {

    private final Method method;

    public HttpRequestHandlerHandler(Method method) {
        this.method = method;
    }

    @Override
    public String handleRequest(HttpMessage request) {
        HttpRequestHandler controller = (HttpRequestHandler) SpringContext.BEAN_MAP.get(method.getDeclaringClass());
        HttpMessage output = new HttpMessage();
        controller.handleRequest(request, output);
        return output.getBody();
    }
}
