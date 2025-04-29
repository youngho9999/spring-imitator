package live.context;

import live.exception.ContextInitializeException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;

public class SpringContextInitiator {

    public void createInstances(Set<Class<?>> scannedComponents) {
        for (Class<?> clazz : scannedComponents) {
            try {
                Object o = clazz.getDeclaredConstructor().newInstance();
                SpringContext.BEAN_MAP.put(clazz, o);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new ContextInitializeException(e);
            }
        }
    }

    public void injectDependencies() {
        for (Class<?> clazz : SpringContext.BEAN_MAP.keySet()) {
            Field[] declaredFields = clazz.getDeclaredFields();

            for (Field field : declaredFields) {
                if (!field.isAnnotationPresent(AutoWire.class))
                    continue;

                Class<?> target = field.getType();
                Object targetInstance = Optional.ofNullable(SpringContext.BEAN_MAP.get(target)).orElseThrow(ContextInitializeException::new);

                field.setAccessible(true);
                try {
                    field.set(SpringContext.BEAN_MAP.get(clazz),targetInstance);
                } catch (IllegalAccessException e) {
                    throw new ContextInitializeException(e);
                }
                field.setAccessible(false);
            }

        }
    }

}
