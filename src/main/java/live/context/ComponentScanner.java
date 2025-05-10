package live.context;


import live.Main;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    public static Set<Class<?>> scanAllComponents() {
        String basePackage = Main.class.getPackage().getName();
        Reflections reflections = new Reflections(basePackage, Scanners.SubTypes.filterResultsBy(c -> true)); // 모든 하위 타입 스캔

        // basePackage 및 그 하위 패키지의 모든 클래스 가져오기
        Set<Class<?>> allTypes = reflections.getSubTypesOf(Object.class); // 모든 클래스는 Object를 상속

        return allTypes.stream()
                .filter(c -> c.isAnnotationPresent(Component.class))
                .filter(c -> !c.isAnnotation())
                .collect(Collectors.toSet());
    }
}
