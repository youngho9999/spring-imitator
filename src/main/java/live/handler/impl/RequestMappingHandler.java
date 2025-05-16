package live.handler.impl;

import live.context.SpringContext;
import live.handler.Body;
import live.handler.Handler;
import live.handler.HttpMessage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHandler implements Handler {

    public RequestMappingHandler(Method method) {
        this.method = method;
    }

    private final Method method;

    @Override
    public String handleRequest(HttpMessage request) {

        Object controller =  SpringContext.BEAN_MAP.get(method.getDeclaringClass());
        String response = "";

        Parameter[] parameters = method.getParameters();
        Object[] argsToPass = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i].isAnnotationPresent(Body.class)) {
                argsToPass[i] = request.getBody();
            }
        }

        try {
            response = (String) method.invoke(controller, argsToPass);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
