package live.domain.whale;

import live.context.Component;

@Component
public class WhaleService {

    public String getWhale() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Blue Whale";
    }
}
