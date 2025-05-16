package live.handler;

import live.context.Component;

@Component
public class DispatcherServlet {

    public String dispatch(HttpMessage request) {

        if(!PathContext.PATH_HANDLER_MAP.containsKey(request.getPath())) {
            throw new RuntimeException("Path not found");
        }

        Handler handler = PathContext.PATH_HANDLER_MAP.get(request.getPath());
        return handler.handleRequest(request);
    }

}
