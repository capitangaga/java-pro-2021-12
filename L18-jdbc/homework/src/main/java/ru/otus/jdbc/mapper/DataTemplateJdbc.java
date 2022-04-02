package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData)
    {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    return createObject(resultSet);
                }
                return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), resultSet -> {
            try {
                List<T> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(createObject(resultSet));
                }
                return result;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).orElse(List.of());
    }

    @Override
    public long insert(Connection connection, T client) {
        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getInsertParams(client));
    }

    @Override
    public void update(Connection connection, T client) {
        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getUpdateParams(client));
    }

    private T createObject(ResultSet resultSet) {
        try {
            T t = entityClassMetaData.getConstructor().newInstance();
            entityClassMetaData.getAllFields().forEach(field -> {
                try {
                    field.set(t, resultSet.getObject(field.getName()));
                } catch (IllegalAccessException | SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e ) {
            // не будем тут сильно заморачиваться, но в нормальной либе, конечно, нужно что-то более информативное
            throw new RuntimeException(e);
        }
    }

    private List<Object> getInsertParams(T object) {
        return entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
            try {
                System.out.println(field.get(object));
                return field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private List<Object> getUpdateParams(T object) {
        Stream<Object> nonIdFields = entityClassMetaData.getFieldsWithoutId().stream().map(field -> {
            try {
                return field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            return Stream.concat(nonIdFields, Stream.of(entityClassMetaData.getIdField().get(object))).toList();
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }

    }
}
