package com.greendao_schema_builder.relationship;

import com.greendao_schema_builder.errors.InvalidEntityException;
import com.greendao_schema_builder.errors.InvalidEntityRelationException;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Builds greenDao entities relation.
 */
public class EntityRelationBuilder {

    private static final String ID_PROPERTY_POSTFIX = "Code";

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
        Class<?> sourceEntityClass = _entityRelation.getSourceEntity();
        for (Entity entity : daoSchema.getEntities()) {
            if (entity.getClassName().equals(sourceEntityClass.getSimpleName())) {
                return entity;
            }
        }
        throw new InvalidEntityException("No such Source Entity: " + sourceEntityClass.getSimpleName());
    }

    /**
     * Returns the relation greenDao Entity of a relation.
     * @param _entityRelation the entity relation.
     * @return the source greenDao Entity of a relation.
     * @throws InvalidEntityException
     */
    private Entity getRelationEntity(EntityRelation _entityRelation) throws InvalidEntityException {
        Class<?> relationEntityClass = _entityRelation.getRelationEntity();
        for (Entity entity : daoSchema.getEntities()) {
            if (entity.getClassName().equals(relationEntityClass.getSimpleName())) {
                return entity;
            }
        }
        throw new InvalidEntityException("No such Relation Entity: " + relationEntityClass.getSimpleName());
    }

    /**
     * Builds a new Entity relation between a source and a relation greenDao entities.
     * @param _entityRelation the entity relation to be built.
     * @throws InvalidEntityRelationException
     * @throws InvalidEntityException
     */
    public void buildRelation(EntityRelation _entityRelation) throws InvalidEntityRelationException, InvalidEntityException {
        Entity sourceEntity = getSourceEntity(_entityRelation);
        Entity relationEntity = getRelationEntity(_entityRelation);

        String relationField = _entityRelation.getRelationFieldNameToCreate();
        Property idProperty = null;
        if (relationField == null){
            idProperty = sourceEntity.addStringProperty(relationEntity.getClassName().toLowerCase()
                    + ID_PROPERTY_POSTFIX).getProperty();
        } else {
            for (Property property : sourceEntity.getProperties()) {
                if (property.getPropertyName().equals(relationField)) {
                    idProperty = property;
                    break;
                }
            }
        }

        if (_entityRelation.getRelationType().equals(EntityRelationType.ONE_TO_MANY)) {
            sourceEntity.addToMany(relationEntity, idProperty, _entityRelation.getRelationFieldName());
        }

        else if (_entityRelation.getRelationType().equals(EntityRelationType.ONE_TO_ONE)) {
            sourceEntity.addToOne(relationEntity, idProperty, _entityRelation.getRelationFieldName());
        }

        else {
            throw new InvalidEntityRelationException(
                "Needs a valid Schema Relation type, should be one of : " + EntityRelationType.ONE_TO_MANY + " " + EntityRelationType.ONE_TO_MANY);
        }
    }
}
