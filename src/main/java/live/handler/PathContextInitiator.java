package live.handler;

import live.context.SpringContext;

import java.lang.reflect.Method;

public class PathContextInitiator {

    public void scanHandlers() {

        for (Class<?> clazz : SpringContext.BEAN_MAP.keySet()) {
            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            //HttpRequestHandler 상속인경우
            if (HttpRequestHandler.class.isAssignableFrom(clazz)) {
                String path = clazz.getAnnotation(Controller.class).value();
                Method method;
                try {
                    method = clazz.getMethod("handleRequest", HttpMessage.class, HttpMessage.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                PathContext.PATH_METHOD_MAP.put(path, method);
                continue;
            }

            //RequestMapping 이용
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String path = method.getAnnotation(RequestMapping.class).value();
                    PathContext.PATH_METHOD_MAP.put(path, method);
                }
            }

        }

    }
}
