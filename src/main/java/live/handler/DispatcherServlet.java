package live.handler;

import live.context.Component;
import live.context.SpringContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
public class DispatcherServlet {

    public String dispatch(String inputPath, String body) {

        if(!PathContext.PATH_METHOD_MAP.containsKey(inputPath)) {
            throw new RuntimeException("Path not found");
        }

        Method method = PathContext.PATH_METHOD_MAP.get(inputPath);
        String outputMessage = "";

        //HttpRequestHadler 형태라면
        if(HttpRequestHandler.class.isAssignableFrom(method.getDeclaringClass())) {
            HttpRequestHandler controller = (HttpRequestHandler) SpringContext.BEAN_MAP.get(method.getDeclaringClass());
            HttpMessage input = new HttpMessage(body);
            HttpMessage output = new HttpMessage();
            controller.handleRequest(input, output);
            outputMessage = output.getMessage();
        }

        //RequestMapping 형태라면
        if(method.isAnnotationPresent(RequestMapping.class)) {
            Object controller =  SpringContext.BEAN_MAP.get(method.getDeclaringClass());

            Parameter[] parameters = method.getParameters();
            Object[] argsToPass = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                if(parameters[i].isAnnotationPresent(Body.class)) {
                    argsToPass[i] = body;
                }
            }

            try {
                outputMessage = (String) method.invoke(controller, argsToPass);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return outputMessage;
    }

}
