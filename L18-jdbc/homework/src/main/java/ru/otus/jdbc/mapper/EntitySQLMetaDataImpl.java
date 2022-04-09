package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;

/**
 * @author kirillgolovko
 */
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.selectAllSql = generateSelectAllSql(entityClassMetaData);
        this.selectByIdSql = generateSelectByIdSql(entityClassMetaData);
        this.insertSql = generateInsertSql(entityClassMetaData);
        this.updateSql = generateUpdateSql(entityClassMetaData);
    }

    private static String generateSelectAllSql(EntityClassMetaData<?> entityClassMetaData) {
        return String.format("SELECT %s FROM %s", getFieldNames(entityClassMetaData), entityClassMetaData.getName());
    }

    private static String generateSelectByIdSql(EntityClassMetaData<?> entityClassMetaData) {
        return String.format(
                "SELECT %s FROM %s WHERE %s = ?",
                getFieldNames(entityClassMetaData),
                entityClassMetaData.getName(),
                entityClassMetaData.getIdField().getName());
    }

    private static String generateInsertSql(EntityClassMetaData<?> entityClassMetaData) {
        return String.format(
                "INSERT INTO %s (%s) VALUES (%s)",
                entityClassMetaData.getName(),
                getFieldNamesWithoutId(entityClassMetaData),
                getParams(entityClassMetaData));
    }

    private static String generateUpdateSql(EntityClassMetaData<?> entityClassMetaData) {
        return String.format(
                "UPDATE %s SET %s WHERE %s = ?",
                entityClassMetaData.getName(),
                getUpdateFields(entityClassMetaData),
                entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }

    private static String getFieldNames(EntityClassMetaData<?> entityClassMetaData) {
        return String.join(", ", entityClassMetaData.getAllFields().stream().map(Field::getName).toList());
    }

    private static String getFieldNamesWithoutId(EntityClassMetaData<?> entityClassMetaData) {
        return String.join(
                ", ",
                entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).toList());
    }

    private static String getParams(EntityClassMetaData<?> entityClassMetaData) {
        return String.join(", ", Collections.nCopies(entityClassMetaData.getFieldsWithoutId().size(), "?"));
    }

    private static String getUpdateFields(EntityClassMetaData<?> entityClassMetaData) {
        return String.join(
                ", ",
                entityClassMetaData.getFieldsWithoutId().stream().map(field -> field.getName() + " = ?").toList());
    }
}
