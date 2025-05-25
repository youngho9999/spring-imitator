package live.domain.whale;

import live.context.AutoWire;
import live.context.Component;
import live.handler.Controller;
import live.handler.RequestMapping;

@Component
@Controller
public class WhaleController {

    private final WhaleService whaleService;

    @AutoWire
    public WhaleController(WhaleService whaleService) {
        this.whaleService = whaleService;
    }

    @RequestMapping("/whale")
    public String getWhale() {
        return whaleService.getWhale();
    }

    @RequestMapping("/fastwhale")
    public String getFastWhale() {
        return "Fast Whale";
    }
}
