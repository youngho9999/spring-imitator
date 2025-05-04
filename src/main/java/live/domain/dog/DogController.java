package live.domain.dog;

import live.context.AutoWire;
import live.context.Component;

@Component
public class DogController {

    private DogService dogService;

    @AutoWire
    public void setDogService(DogService dogService) {
        this.dogService = dogService;
    }

    public void bark() {
        dogService.bark();
    }
}
