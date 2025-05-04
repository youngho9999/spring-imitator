package live.domain.eagle;

import live.context.AutoWire;
import live.context.Component;

@Component
public class EagleController {

    private final BlackEagleService blackEagleService;
    private final WhiteEagleService whiteEagleService;

    @AutoWire
    public EagleController(BlackEagleService blackEagleService, WhiteEagleService whiteEagleService) {
        this.blackEagleService = blackEagleService;
        this.whiteEagleService = whiteEagleService;
    }

    public void fly() {
        blackEagleService.fly();
        whiteEagleService.fly();
    }
}
