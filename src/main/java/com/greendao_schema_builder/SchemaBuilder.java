package com.greendao_schema_builder;

import com.greendao_schema_builder.errors.InvalidEntityException;
import com.greendao_schema_builder.errors.InvalidEntityRelationException;
import com.greendao_schema_builder.property.EntityPropertiesBuilder;
import com.greendao_schema_builder.property.PropertyOptions;
import com.greendao_schema_builder.relationship.EntityRelation;
import com.greendao_schema_builder.relationship.EntityRelationBuilder;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a greenDao schema based on model classes,
 * excluding blacklisted fields,
 * including relation.
 */
public class SchemaBuilder {

    private final Schema schema;
    private final String outDirectory;
    private final List<String> blackListFields;
    private final EntityPropertiesBuilder entityPropertiesBuilder;
    private final EntityRelationBuilder entityRelationBuilder;

    private List<PropertyOptions> propertyOptionsList;
    private List<EntityRelation> entityRelations;

    /**
     * Constructor.
     * @param _version The greenDao Schema Version.
     * @param _defaultJavaPackage The default Java package.
     * @param _outDirectory The directory which the schema will be generated.
     */
    public SchemaBuilder(
            int _version,
            String _defaultJavaPackage,
            String _outDirectory)
    {
        outDirectory = _outDirectory;
        schema = new Schema(_version, _defaultJavaPackage);
        blackListFields = new ArrayList<String>();
        entityPropertiesBuilder = new EntityPropertiesBuilder(schema);
        entityRelationBuilder = new EntityRelationBuilder(schema);

        propertyOptionsList = new ArrayList<PropertyOptions>();
        entityRelations = new ArrayList<EntityRelation>();
    }

    /**
     * Add a new Entity Options object.
     * @param _options the option to be added.
     */
    public void addEntityProperty(PropertyOptions _options) {
        if (!propertyOptionsList.contains(_options)) {
            propertyOptionsList.add(_options);
        }
    }

    /**
     * Add a new Entity relation.
     * @param _relation the relation to be added.
     */
    public void addEntityRelation(EntityRelation _relation) {
        if (!entityRelations.contains(_relation)) {
            entityRelations.add(_relation);

            // Add the relation to blacklist in order not to recreate it.
            addFieldToBlackList(_relation.getRelationFieldName());
        }
    }

    /**
     * Build properties for an greenDaoEntity from a class.
     * @param _options the options.
     * @return the greenDao Entity.
     * @throws InvalidClassException
     */
    private Entity buildProps(PropertyOptions _options)
        throws InvalidClassException
    {
        Entity entity;

        // Extract options
        final Class entityClass = _options.getEntityClass();
        final String primaryKey = _options.getPrimaryKey();
        final String[] interfaces = _options.getInterfaces();
        final String superClassName = _options.getSuperClassName();

        if (primaryKey != null) {
            entity = entityPropertiesBuilder
                    .buildPropertiesForEntity(entityClass, blackListFields, primaryKey);
        } else {
            entity = entityPropertiesBuilder
                    .buildPropertiesForEntity(entityClass, blackListFields);
        }

        if (interfaces.length > 0) {
            entity.implementsInterface(interfaces);
        }

        if (superClassName != null) {
            entity.setSuperclass(superClassName);
        }

        return entity;
    }

    /**
     * Build a greenDao Entity relation.
     * @param _entityRelation the relation object.
     * @throws InvalidEntityRelationException
     * @throws InvalidEntityException
     */
    private void buildEntityRelation(EntityRelation _entityRelation) throws InvalidEntityRelationException, InvalidEntityException {
        entityRelationBuilder.buildRelation(_entityRelation);
    }

    /**
     * Generate greenDao schema, Should be called last.
     * @throws Exception
     */
    public void generate() throws Exception {
        // Build the properties.
        for (PropertyOptions propertyOptions : propertyOptionsList) {
            buildProps(propertyOptions);
        }

        // Build the relations.
        for (EntityRelation entityRelation : entityRelations) {
            buildEntityRelation(entityRelation);
        }

        // Generate the greenDao schema.
        generateDao(schema, outDirectory);
    }

    /**
     * Generate dao.
     * @param _schema the greenDao schema to be generated.
     * @param _outDirectory The directory which the schema will be generated.
     * @throws Exception
     */
    private void generateDao(Schema _schema, String _outDirectory) throws Exception {
        DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generateAll(_schema, _outDirectory);
    }

    /**
     * Adds a fieldName to blacklist in order not to create a Property out of it.
     * @param _fieldName the field name
     */
    public void addFieldToBlackList(String _fieldName) {
        if (!blackListFields.contains(_fieldName)) {
            blackListFields.add(_fieldName);
        }
    }
}