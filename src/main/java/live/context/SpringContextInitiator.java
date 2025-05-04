package live.context;

import live.exception.ContextInitializeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SpringContextInitiator {

    private static Map<Class<?>, Integer> clazzInDegree;
    private static Map<Class<?>, Set<Class<?>>> clazzGraph;

    public void init(Set<Class<?>> scannedComponents) {
        createInstancesForNoConstructorInjectionRequired(scannedComponents);
        injectToFields();
        injectBySetter();
        Queue<Class<?>> topologyQueue = initializeInDegreeForConstructor(scannedComponents);
        int initializedInstances = injectConstructorWithTopologySort(topologyQueue);

        if(initializedInstances != scannedComponents.size()) {
            throw new ContextInitializeException("cycle detected: " + scannedComponents.size() + " components were initialized");
        }
    }

    public void createInstancesForNoConstructorInjectionRequired(Set<Class<?>> scannedComponents) {
        Set<Class<?>> toRemove = new HashSet<>();

        clazzloop:
        for (Class<?> clazz : scannedComponents) {
            //create only for instance without constructor injection
            for (Constructor<?> constructor : clazz.getConstructors()) {
                if (constructor.isAnnotationPresent(AutoWire.class)) {
                    continue clazzloop;
                }
            }

            try {
                clazz.getDeclaredConstructor().newInstance();
                Object o = clazz.getDeclaredConstructor().newInstance();
                SpringContext.BEAN_MAP.put(clazz, o);
                toRemove.add(clazz);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new ContextInitializeException(e);
            }
        }

        scannedComponents.removeAll(toRemove);
    }

    public Queue<Class<?>> initializeInDegreeForConstructor(Set<Class<?>> scannedComponents) {
        //진입차수 저장 Map
        clazzInDegree = new HashMap<>();
        //그래프 탐색을 위한 Map
        clazzGraph = new HashMap<>();

        //initialize in degree for all classes
        for (Class<?> clazz : scannedComponents) {
            initializeEachClassForTopologySort(clazz);
        }

        Queue<Class<?>> topologySortQueue = new ArrayDeque<>();
        for(Class<?> clazz : clazzInDegree.keySet()) {
            if(clazzInDegree.get(clazz) != 0) {
                continue;
            }
            topologySortQueue.add(clazz);
        }

        return topologySortQueue;
    }

    private void initializeEachClassForTopologySort(Class<?> clazz) {

        Constructor<?> constructor = Arrays.stream(clazz.getConstructors())
                .filter(c -> c.isAnnotationPresent(AutoWire.class))
                .findFirst()
                .orElseThrow(() -> new ContextInitializeException("No AutoWire constructor found for " + clazz.getName()));

        Class<?>[] parameterTypes = constructor.getParameterTypes();
        int inDegreeCount = 0;

        for(Class<?> parameter : parameterTypes) {
            if(SpringContext.BEAN_MAP.containsKey(parameter)) {
                continue;
            }
            inDegreeCount++;

            //이후 위상정렬 그래프 탐색을 위해 탐색 방향 설정
            if(!clazzGraph.containsKey(parameter)) {
                clazzGraph.put(parameter, new HashSet<>());
            }

            clazzGraph.get(parameter).add(clazz);

        }

        clazzInDegree.put(clazz, inDegreeCount);
    }

    private int injectConstructorWithTopologySort(Queue<Class<?>> topologySortQueue) {
        int initializedInstances = 0;

        while(!topologySortQueue.isEmpty()) {
            Class<?> clazz = topologySortQueue.poll();

            Constructor<?> constructor = Arrays.stream(clazz.getConstructors())
                    .filter(c -> c.isAnnotationPresent(AutoWire.class))
                    .findFirst()
                    .orElseThrow(() -> new ContextInitializeException("No AutoWire constructor found for " + clazz.getName()));

            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] injectInstances = new Object[parameterTypes.length];

            for(int i = 0; i < parameterTypes.length; i++) {
                injectInstances[i] = Optional.ofNullable(SpringContext.BEAN_MAP.get(parameterTypes[i])).orElseThrow(ContextInitializeException::new);
            }

            try {
                Object injectedInstance = constructor.newInstance(injectInstances);
                SpringContext.BEAN_MAP.put(clazz, injectedInstance);
                initializedInstances++;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ContextInitializeException(e);
            }

            //다음 대상 확인
            if(!clazzGraph.containsKey(clazz)) {
                continue;
            }

            for(Class<?> nextClazz : clazzGraph.get(clazz)) {
                Integer nextClazzIndegree = clazzInDegree.computeIfPresent(nextClazz, (k, v) -> v - 1);
                if(nextClazzIndegree == 0) {
                    topologySortQueue.add(nextClazz);
                }
            }
        }

        return initializedInstances;
    }

    private void injectBySetter() {
        for (Class<?> clazz : SpringContext.BEAN_MAP.keySet()) {
            Method[] methods = clazz.getMethods();

            for (Method method : methods) {
                if(!method.isAnnotationPresent(AutoWire.class)) {
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                Object[] injectInstances = new Object[parameterTypes.length];

                for(int i = 0; i < parameterTypes.length; i++) {
                    injectInstances[i] = Optional.ofNullable(SpringContext.BEAN_MAP.get(parameterTypes[i])).orElseThrow(ContextInitializeException::new);
                }

                try {
                    method.invoke(SpringContext.BEAN_MAP.get(clazz), injectInstances);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new ContextInitializeException(e);
                }
            }
        }
    }

    private void injectToFields() {
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
