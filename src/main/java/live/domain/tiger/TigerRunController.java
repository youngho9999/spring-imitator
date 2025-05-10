package live.domain.tiger;

import live.handler.Body;
import live.handler.Controller;
import live.handler.RequestMapping;

@Controller
public class TigerRunController {

    @RequestMapping("/path")
    public void tigerRun(@Body String body) {
        System.out.println(body + " is Running");
    }
}
