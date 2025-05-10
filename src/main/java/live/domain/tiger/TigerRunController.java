package live.domain.tiger;

import live.context.Component;
import live.handler.Body;
import live.handler.Controller;
import live.handler.RequestMapping;

@Component
@Controller
public class TigerRunController {

    @RequestMapping("/run")
    public String tigerRun(@Body String body) {
        return body + " is Running";
    }
}
