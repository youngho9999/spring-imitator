package live.domain.eagle;

import live.context.AutoWire;
import live.context.Component;

@Component
public class BlackEagleService {

    private final BlackEagleRepository blackEagleRepository;

    @AutoWire
    public BlackEagleService(BlackEagleRepository blackEagleRepository) {
        this.blackEagleRepository = blackEagleRepository;
    }

    public void fly() {
        System.out.println("black eagle flying");
    }
}
