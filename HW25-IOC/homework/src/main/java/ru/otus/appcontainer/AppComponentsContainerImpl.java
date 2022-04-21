package ru.otus.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(List.of(initialConfigClass));
    }

    public AppComponentsContainerImpl(List<Class<?>> configs) {
        processConfig(configs);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.TypesAnnotated)
                .filterInputsBy(new FilterBuilder().add(str -> str.endsWith("class")))
        );
        processConfig(reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class).stream().toList());
    }

    private void processConfig(List<Class<?>> configClasses) {
        configClasses.forEach(this::checkConfigClass);
        configClasses.stream()
                .sorted(Comparator.comparing(conf -> conf.getAnnotation(AppComponentsContainerConfig.class).order()))
                .flatMap(clazz ->
                        getComponentsMethods(clazz).stream().map(m -> AppComponentGetterMethod.fromMethod(clazz, m)))
                .sorted(Comparator.comparing(AppComponentGetterMethod::getOrder))
                .forEach(compMethod -> {
                    Object component = compMethod.getComponent(this::getAppComponent);
                    appComponents.add(component);
                    appComponentsByName.put(compMethod.getName(), component);
                });
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(comp -> componentClass.isAssignableFrom(comp.getClass()))
                .findFirst()
                .orElse(null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.getOrDefault(componentName, null);
    }

    private static List<Method> getComponentsMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .toList();
    }



    private static class AppComponentGetterMethod {
        private final Class<?> configClass;
        private final Method method;
        private int order;
        private String name;
        private List<Class<?>> requiredComponents;

        private AppComponentGetterMethod(
                Class<?> configClass,
                 Method method,
                int order,
                String name,
                List<Class<?>> requiredComponents)
        {
            this.configClass = configClass;
            this.method = method;
            this.order = order;
            this.name = name;
            this.requiredComponents = requiredComponents;
        }

        private static AppComponentGetterMethod fromMethod(Class<?> configClass, Method method) {
            method.setAccessible(true);
            AppComponent annotation = method.getAnnotation(AppComponent.class);
            int order = annotation.order();
            String name = annotation.name();
            List<Class<?>> requiredComponents = List.of(method.getParameterTypes());
            return new AppComponentGetterMethod(configClass, method, order, name, requiredComponents);
        }

        public int getOrder() {
            return order;
        }

        public String getName() {
            return name;
        }

        public Object getComponent(Function<Class<?>, Object> createdComponentsGetter) {
            Object[] requiredComponentsImpl = requiredComponents.stream().map(createdComponentsGetter).toArray();
            Object config = getConfigObject(configClass);
            try {
                return method.invoke(config, requiredComponentsImpl);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        private static Object getConfigObject(Class<?> configClass) {
            try {
                return configClass.getConstructor().newInstance();
            } catch (NoSuchMethodException
                    | InvocationTargetException
                    | InstantiationException
                    | IllegalAccessException e)
            {
                throw new IllegalArgumentException(
                        "No args constructor is required for config: " + configClass.getName(),
                        e);
            }
        }
    }
}
