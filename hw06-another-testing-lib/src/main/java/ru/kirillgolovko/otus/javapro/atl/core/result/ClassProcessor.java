package ru.kirillgolovko.otus.javapro.atl.core.result;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import ru.kirillgolovko.otus.javapro.atl.annotations.After;
import ru.kirillgolovko.otus.javapro.atl.annotations.Before;
import ru.kirillgolovko.otus.javapro.atl.annotations.Disabled;
import ru.kirillgolovko.otus.javapro.atl.annotations.Test;
import ru.kirillgolovko.otus.javapro.atl.core.DisabledTestRunner;
import ru.kirillgolovko.otus.javapro.atl.core.RealTestRunner;
import ru.kirillgolovko.otus.javapro.atl.core.TestRunner;

public class ClassProcessor {
    private static final Set<Class<? extends Annotation>> ONLY_ONE_ANNOTATION
            = Set.of(After.class, Before.class, Test.class);

    public static List<Supplier<TestResult>> getTestResultSuppliers(Class<?> clazz) {
        Optional<Method> beforeO = getAndValidateSingle(Before.class, clazz);
        Optional<Method> afterO = getAndValidateSingle(After.class, clazz);
        List<Method> classTestMethods = getClassTestMethods(clazz);
        List<Supplier<TestResult>> result = new ArrayList<>(classTestMethods.size());
        for (var method : classTestMethods) {
            result.add(() ->
                    getTestRunner(clazz, method, beforeO.orElse(null), afterO.orElse(null)).executeTest());
        }
        return result;
    }

    private static List<Method> getClassTestMethods(Class<?> clazz) {
        List<Method> methods = Arrays
                .stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .toList();
        methods.forEach(method -> validateMethodSingleAnnotation(method, clazz));
        methods.forEach(method -> validateMethodSignature(method, clazz));
        return methods;
    }

    private static <AnnotationType extends Annotation> Optional<Method> getAndValidateSingle(
            Class<AnnotationType> annotatedWith,
            Class<?> clazz)
    {
        List<Method> found = Arrays
                .stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotatedWith)).toList();

        // Maybe no @Before or @After
        if (found.isEmpty()) {
            return Optional.empty();
        }

        if (found.size() != 1) {
            throw new IllegalStateException(String.format(
                    "Found more than one method annotated %s for class %s",
                    annotatedWith.getName(),
                    clazz.getCanonicalName()));
        }
        Method method = found.stream().findFirst().get();
        validateMethodSingleAnnotation(method, clazz);
        validateMethodSignature(method, clazz);
        if (isDisabled(method)) {
            return Optional.empty();
        }
        return Optional.of(method);
    }

    private static void validateMethodSignature(Method method, Class<?> clazz) {
        int modifiers = method.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            throw new IllegalStateException(String
                    .format("Expected method %s in class %s to be public", method.getName(), clazz.getCanonicalName()));
        }
        if (Modifier.isAbstract(modifiers)) {
            throw new IllegalStateException(String.format(
                    "Expected method %s in class %s to be not abstract",
                    method.getName(),
                    clazz.getCanonicalName()));
        }
        if (Modifier.isStatic(modifiers)) {
            throw new IllegalStateException(String.format(
                    "Expected method %s in class %s to be not static",
                    method.getName(),
                    clazz.getCanonicalName()));
        }
        if (!method.getReturnType().equals(Void.TYPE)) {
            throw new IllegalStateException(String.format(
                    "Expected method %s in class %s to be void",
                    method.getName(),
                    clazz.getCanonicalName()));
        }
        if (method.getParameterCount() != 0) {
            throw new IllegalStateException(String.format(
                    "Expected method %s in class %s to have no params",
                    method.getName(),
                    clazz.getCanonicalName()));
        }
    }

    private static void validateMethodSingleAnnotation(Method method, Class<?> clazz) {
        if (Arrays
                .stream(method.getAnnotations())
                .map(Annotation::annotationType)
                .filter(ONLY_ONE_ANNOTATION::contains).count() != 1)
        {
            throw new IllegalStateException(String.format(
                    "Expected only one annotation on method %s in class %s",
                    method.getName(),
                    clazz.getCanonicalName()));
        }
    }

    private static boolean isDisabled(Method method) {
        return method.isAnnotationPresent(Disabled.class);
    }

    private static TestRunner getTestRunner(Class<?> clazz, Method test, Method before, Method after) {
        String name = test.getAnnotation(Test.class).name();
        if (name.isBlank()) {
            name = test.getDeclaringClass().getCanonicalName() + "#" + test.getName();
        }
        if (isDisabled(test)) {
            return new DisabledTestRunner(name);
        } else {
            Object instanceToRunTest = getInstance(clazz);
            Runnable testRunnable = wrapMethodInvocation(test, instanceToRunTest);
            Runnable beforeRunnable = before == null ? () -> {} : wrapMethodInvocation(before, instanceToRunTest);
            Runnable afterRunnable = after == null ? () -> {} : wrapMethodInvocation(after, instanceToRunTest);
            return new RealTestRunner(name, beforeRunnable, testRunnable, afterRunnable);
        }
    }

    private static Object getInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ex)
        {
            throw new IllegalStateException(
                    String.format(
                            "Class %s should provide default or no params public constructor",
                            clazz.getCanonicalName()),
                    ex);
        }
    }

    private static Runnable wrapMethodInvocation(Method method, Object object) {
        return () -> {
            try {
                method.invoke(object);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(
                        String.format("Failed method %s invocation", method.getName()), ex.getCause());
            }
        };
    }
}
