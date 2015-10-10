package com.greendao_schema_builder.relationship;

/**
 * A greenDat relation representation.
 */
public class EntityRelation {

    // The entity that has the relation.
    private final Class<?> sourceEntity;

    // The entity that is related to the sourceEntity.
    private final Class<?> relationEntity;

    // The fieldName which is related on the sourceEntity with the relationEntity.
    private final String relationFieldName;

    // The fieldName of the relative relationFieldName in order to relate the tables.
    private String relationFieldNameToCreate;

    // The type of the relation.
    private final EntityRelationType relationType;

    /**
     * Constructor.
     * @param _sourceEntity The entity that has the relation.
     * @param _relationEntity The entity that is related to the sourceEntity.
     * @param _relationFieldName The fieldName which is related on the sourceEntity with the relationEntity.
     * @param _relationFieldName The fieldName of the relative relationFieldName in order to relate the tables.
     * @param _relationType The type of the relation.
     */
    public EntityRelation(
            Class<?> _sourceEntity,
            Class<?> _relationEntity,
            String _relationFieldName,
            EntityRelationType _relationType,
            String _relationFieldNameToCreate)
    {
        sourceEntity = _sourceEntity;
        relationEntity = _relationEntity;
        relationFieldName = _relationFieldName;
        relationType = _relationType;
        relationFieldNameToCreate = _relationFieldNameToCreate;
    }

    public EntityRelation(
            Class<?> _sourceEntity,
            Class<?> _relationEntity,
            String _relationFieldName,
            EntityRelationType _relationType)
    {
        sourceEntity = _sourceEntity;
        relationEntity = _relationEntity;
        relationFieldName = _relationFieldName;
        relationType = _relationType;
    }

    public Class<?> getSourceEntity() {
        return sourceEntity;
    }

    public Class<?> getRelationEntity() {
        return relationEntity;
    }

    public String getRelationFieldName() {
        return relationFieldName;
    }

    public String getRelationFieldNameToCreate() {
        return relationFieldNameToCreate;
    }

    public EntityRelationType getRelationType() {
        return relationType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final EntityRelation other = (EntityRelation) obj;

        if (this.sourceEntity != other.sourceEntity) {
            return false;
        }

        if (this.relationEntity != other.relationEntity) {
            return false;
        }

        if (this.relationFieldName != other.relationFieldName) {
            return false;
        }

        if (this.relationType != other.relationType) {
            return false;
        }

        return true;
    }
}
