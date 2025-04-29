package live;

import live.context.ComponentScanner;
import live.context.SpringContext;
import live.context.SpringContextInitiator;
import live.domain.CatController;
import live.domain.CatService;

import java.lang.reflect.Field;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Class<?>> scannedComponents = ComponentScanner.scanAllComponents();

        SpringContextInitiator initiator = new SpringContextInitiator();
        initiator.createInstances(scannedComponents);
        initiator.injectDependencies();

        CatController o = (CatController) SpringContext.BEAN_MAP.get(CatController.class);
        o.cat();


    }
}