package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import ru.otus.core.annotations.Id;

/**
 * @author kirillgolovko
 */
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T>{

    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.name = clazz.getSimpleName();
        this.constructor = generateConstructor(clazz);
        this.idField = generateIdField(clazz);
        this.allFields = generateAllFields(clazz);
        this.fieldsWithoutId = generateFieldsWithoutId(clazz);
    }

    private static <T> Constructor<T> generateConstructor(Class<T> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    String.format("Class %s must have default no args constructor", clazz.getCanonicalName()));
        }
    }

    private static <T> Field generateIdField(Class<T> clazz) {
        List<Field> annotatedFields = Arrays
                .stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .toList();
        if (annotatedFields.size() != 1) {
            throw new IllegalArgumentException(
                    String.format("Class %s must have one and only @Id field", clazz.getCanonicalName()));
        }
        return annotatedFields.stream().peek(field -> field.setAccessible(true)).findFirst().get();
    }

    private static <T> List<Field> generateAllFields(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).peek(field -> field.setAccessible(true)).toList();
    }

    private static <T> List<Field> generateFieldsWithoutId(Class<T> clazz) {
        return generateAllFields(clazz).stream().filter(field -> !field.isAnnotationPresent(Id.class)).toList();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
