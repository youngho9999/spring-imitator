package live.handler;

import live.context.Component;
import live.context.SpringContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Component
public class DispatcherServlet {

    public String dispatch(HttpMessage request) {

        if(!PathContext.PATH_METHOD_MAP.containsKey(request.getPath())) {
            throw new RuntimeException("Path not found");
        }

        Method method = PathContext.PATH_METHOD_MAP.get(request.getPath());
        String response = "";

        //HttpRequestHadler 형태라면
        if(HttpRequestHandler.class.isAssignableFrom(method.getDeclaringClass())) {
            HttpRequestHandler controller = (HttpRequestHandler) SpringContext.BEAN_MAP.get(method.getDeclaringClass());
            HttpMessage output = new HttpMessage();
            controller.handleRequest(request, output);
            response = output.getBody();
        }

        //RequestMapping 형태라면
        if(method.isAnnotationPresent(RequestMapping.class)) {
            Object controller =  SpringContext.BEAN_MAP.get(method.getDeclaringClass());

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
        }

        return response;
    }

}
