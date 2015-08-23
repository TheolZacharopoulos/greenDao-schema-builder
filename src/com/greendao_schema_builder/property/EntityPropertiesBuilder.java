package com.greendao_schema_builder.property;

import com.greendao_schema_builder.utils.SchemaGenUtils;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.PropertyType;
import de.greenrobot.daogenerator.Schema;

import java.io.InvalidClassException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds properties of a greenDao Entity based on a given class, uses reflection.
 */
public class EntityPropertiesBuilder {

    private static final String ENUMS_PREFIX = "AsString";
    private static final String PRIMARY_KEY_DEFAULT_NAME = "Id";

    // The greenDao schema.
    private final Schema daoSchema;

    // The entities that have already added to the schema.
    private final List<Class<?>> addedEntityClasses;

    /**
     * Constructor.
     * @param _daoSchema the greenDao Schema.
     */
    public EntityPropertiesBuilder(Schema _daoSchema) {
        daoSchema = _daoSchema;
        addedEntityClasses = new ArrayList<Class<?>>();
    }

    /**
     * Adds a new Dao entity to the schema, with name the class's simple name.
     * @param _entityClass the entity class to be added.
     * @return the added Dao Entity.
     */
    private Entity addEntityToSchema(Class<?> _entityClass) {
        return addEntityToSchema(_entityClass, _entityClass.getSimpleName());
    }

    /**
     * Adds a new Dao entity to the schema with a given name.
     * @param _entityClass the entity class to be added.
     * @param _entityName the entity's name.
     * @return the added Dao Entity.
     */
    private Entity addEntityToSchema(Class<?> _entityClass, String _entityName) {
        return daoSchema.addEntity(_entityName);
    }

    /**
     * Builds the properties of a greenDao Entity based on the given class, using reflection.
     * @param _entityClass the class to take the properties from.
     * @param _blackListFields a list of fields that can be excluded.
     * @return the new greenDao Entity.
     * @throws InvalidClassException
     */
    public Entity buildPropertiesForEntity(Class<?> _entityClass, List<String> _blackListFields) throws InvalidClassException {
        return buildPropertiesForEntity(_entityClass, _blackListFields, PRIMARY_KEY_DEFAULT_NAME);
    }

    /**
     * Builds the properties of a greenDao Entity based on the given class, using reflection.
     * @param _entityClass the class to take the properties from.
     * @param _blackListFields a list of fields that can be excluded.
     * @param _primaryKeyName a field name which will marked as primary key of this entity.
     *  @throws InvalidClassException
     * @return the new greenDao Entity.
     */
    public Entity buildPropertiesForEntity(
        Class<?> _entityClass,
        List<String> _blackListFields,
        String _primaryKeyName)
    throws InvalidClassException
    {

        if (_entityClass == null) {
            throw new InvalidClassException("The entity class is null.");
        }

        // default primary key.
        if (_primaryKeyName == null) {
            _primaryKeyName = PRIMARY_KEY_DEFAULT_NAME;
        }

        // If the class is already added, return.
        if (addedEntityClasses.contains(_entityClass)) {
            return null;
        }

        // add class.
        addedEntityClasses.add(_entityClass);

        // create a new greenDao entity.
        Entity greenDaoEntity = addEntityToSchema(_entityClass);

        // Get all the fields of the given Class included those of it's superclass recursively.
        Field[] allFields = SchemaGenUtils.getSuperFieldsRecursively(_entityClass, new Field[]{});

        // Add fields to the greenDao Entity.
        for (Field field : allFields) {
            Class fieldType = field.getType();
            String fieldTypeName = fieldType.getSimpleName();

            PropertyType propertyType = null;
            String fieldName = field.getName();

            // Ignore field Blacklist.
            if (_blackListFields.contains(fieldName)) {
                continue;
            }

            // There is no "Integer" into the PropertyType, there is PropertyType.Int, so convert it.
            else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                propertyType = PropertyType.Int;
            }

            // Convert the Enums to Strings.
            else if (((Class<?>) fieldType).isEnum()) {
                propertyType = PropertyType.String;
                greenDaoEntity.addProperty(propertyType, fieldName + ENUMS_PREFIX);
                continue;
            }

            // Convert from the string field name to the PropertyType Enum.
            else {
                propertyType = PropertyType.valueOf(fieldTypeName);
            }

            // Add the primary key, if this is the field.
            if (fieldName.equalsIgnoreCase(_primaryKeyName)) {
                greenDaoEntity.addProperty(propertyType, fieldName).primaryKey();
            } else {
                greenDaoEntity.addProperty(propertyType, fieldName);
            }
        }

        return greenDaoEntity;
    }
}
