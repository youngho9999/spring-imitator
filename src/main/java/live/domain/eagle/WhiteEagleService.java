package live.domain.eagle;

import live.context.AutoWire;
import live.context.Component;

@Component
public class WhiteEagleService {

    private final WhileEagleRepository repository;

    @AutoWire
    public WhiteEagleService(WhileEagleRepository repository) {
        this.repository = repository;
    }

    public void fly() {
        System.out.println("white eagle flying");
    }
}
