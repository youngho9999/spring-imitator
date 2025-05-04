package live;

import live.context.ComponentScanner;
import live.context.SpringContext;
import live.context.SpringContextInitiator;
import live.domain.cat.CatController;
import live.domain.dog.DogController;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // 1. 컴포넌트 스캔
        Set<Class<?>> scannedComponents = ComponentScanner.scanAllComponents();

        // 2. 컨텍스트 초기화 및 의존성 주입
        SpringContextInitiator initiator = new SpringContextInitiator();
        initiator.createInstances(scannedComponents);
        initiator.injectDependencies();

        //테스트
        CatController o = (CatController) SpringContext.BEAN_MAP.get(CatController.class);
        o.cat();

        DogController dog = (DogController) SpringContext.BEAN_MAP.get(DogController.class);
        dog.bark();

    }
}