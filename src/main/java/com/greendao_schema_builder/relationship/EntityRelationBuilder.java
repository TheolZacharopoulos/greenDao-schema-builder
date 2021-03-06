package com.greendao_schema_builder.relationship;

import com.greendao_schema_builder.errors.InvalidEntityException;
import com.greendao_schema_builder.errors.InvalidEntityRelationException;

import com.greendao_schema_builder.property.PropertyOptions;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Builds greenDao entities relation.
 */
public class EntityRelationBuilder {

    private static final String ID_PROPERTY_POSTFIX = "Code";

    private static final String CODE = "code";

    // the greenDao Schema
    private final Schema daoSchema;

    /**
     * Constructor.
     * @param _schema the greenDao Schema
     */
    public EntityRelationBuilder(Schema _schema) {
        daoSchema = _schema;
    }

    /**
     * Returns the source greenDao Entity of a relation.
     * @param _entityRelation the entity relation.
     * @return the target greenDao Entity of a relation.
     * @throws InvalidEntityException
     */
    private Entity getSourceEntity(EntityRelation _entityRelation) throws InvalidEntityException {
        final Class<?> sourceEntityClass = _entityRelation.getSourceEntity();

        for (Entity entity : daoSchema.getEntities()) {
            if (entity.getClassName().equals(PropertyOptions.PREFIX_ENTITY_NAME + sourceEntityClass.getSimpleName())) {
                return entity;
            }
        }

        throw new InvalidEntityException("No such Source Entity: " + PropertyOptions.PREFIX_ENTITY_NAME +  sourceEntityClass.getSimpleName());
    }

    /**
     * Returns the relation greenDao Entity of a relation.
     * @param _entityRelation the entity relation.
     * @return the source greenDao Entity of a relation.
     * @throws InvalidEntityException
     */
    private Entity getRelationEntity(EntityRelation _entityRelation) throws InvalidEntityException {
        final Class<?> relationEntityClass = _entityRelation.getRelationEntity();

        for (Entity entity : daoSchema.getEntities()) {
            if (entity.getClassName().equals(PropertyOptions.PREFIX_ENTITY_NAME + relationEntityClass.getSimpleName())) {
                return entity;
            }
        }

        throw new InvalidEntityException("No such Relation Entity: " + PropertyOptions.PREFIX_ENTITY_NAME + relationEntityClass.getSimpleName());
    }

    /**
     * Returns the Id property of the OneToMany relation.
     * @param _entityRelation The relation to get the Id property from.
     * @return the Id Property.
     * @throws InvalidEntityException
     */
    private Property getIdPropertyOneToMany(EntityRelation _entityRelation)
            throws InvalidEntityException
    {
        final Entity sourceEntity   = getSourceEntity(_entityRelation);
        final Entity relationEntity = getRelationEntity(_entityRelation);
        final String relationField  = _entityRelation.getRelationFieldNameToCreate();

        // if there is no relation field, construct the property.
        if (relationField == null) {
            return relationEntity.addStringProperty(
                    sourceEntity.getClassName().toLowerCase() + ID_PROPERTY_POSTFIX).notNull().getProperty();
        }

        // If there is relation field, get the property from there
        for (Property property : relationEntity.getProperties()) {
            if (property.getPropertyName().equals(relationField)) {
                return property;
            }
        }

        // otherwise, add the relationField.
        return relationEntity.addStringProperty(relationField).notNull().getProperty();
    }

    /**
     * Returns the Id property of the OneToOne relation.
     * @param _entityRelation The relation to get the Id property from.
     * @return the Id Property.
     * @throws InvalidEntityException
     */
    private Property getIdPropertyOneToOne(EntityRelation _entityRelation)
            throws InvalidEntityException
    {
        final Entity sourceEntity   = getSourceEntity(_entityRelation);
        return sourceEntity.addStringProperty(_entityRelation.getRelationFieldName()).getProperty();
    }

    /**
     * Builds a new Entity relation between a source and a relation greenDao entities.
     * @param _entityRelation the entity relation to be built.
     * @throws InvalidEntityRelationException
     * @throws InvalidEntityException
     */
    public void buildRelation(EntityRelation _entityRelation)
            throws InvalidEntityRelationException, InvalidEntityException
    {
        final Entity sourceEntity   = getSourceEntity(_entityRelation);
        final Entity relationEntity = getRelationEntity(_entityRelation);

        if (_entityRelation.getRelationType().equals(EntityRelationType.ONE_TO_MANY)) {
            final Property idProperty   = getIdPropertyOneToMany(_entityRelation);
            sourceEntity.addToMany(relationEntity, idProperty, _entityRelation.getRelationFieldName());
        }

        else if (_entityRelation.getRelationType().equals(EntityRelationType.ONE_TO_ONE)) {
            final Property idProperty   = getIdPropertyOneToOne(_entityRelation);
            sourceEntity.addToOne(relationEntity, idProperty);
        }

        else {
            throw new InvalidEntityRelationException(
                "Needs a valid Schema Relation type, should be one of : " + EntityRelationType.ONE_TO_MANY + " " + EntityRelationType.ONE_TO_MANY);
        }
    }
}
