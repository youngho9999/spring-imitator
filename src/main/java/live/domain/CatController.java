package live.domain;

import live.context.AutoWire;
import live.context.Component;

@Component
public class CatController {

    @AutoWire
    private CatService catService;

    public void cat() {
        catService.cat();
    }
}
