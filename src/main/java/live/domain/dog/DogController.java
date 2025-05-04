package live.domain.dog;

import live.context.Component;

@Component
public class DogController {

    private DogService dogService;

    public void bark() {
        dogService.bark();
    }
}
