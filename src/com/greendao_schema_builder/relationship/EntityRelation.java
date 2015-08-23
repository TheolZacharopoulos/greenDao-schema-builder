package com.greendao_schema_builder.relationship;

/**
 * A greenDat relation representation.
 */
public class EntityRelation {

    // The entity that has the relation.
    private final Class<?> targetEntity;

    // The entity that is related to the targetEntity.
    private final Class<?> relationEntity;

    // The fieldName which is related on the targetEntity with the relationEntity.
    private final String relationFieldName;

    // The type of the relation.
    private final EntityRelationType relationType;

    /**
     * Constructor.
     * @param _targetEntity The entity that has the relation.
     * @param _relationEntity The entity that is related to the targetEntity.
     * @param _relationFieldName The fieldName which is related on the targetEntity with the relationEntity.
     * @param _relationType The type of the relation.
     */
    public EntityRelation(
            Class<?> _targetEntity,
            Class<?> _relationEntity,
            String _relationFieldName,
            EntityRelationType _relationType)
    {
        targetEntity = _targetEntity;
        relationEntity = _relationEntity;
        relationFieldName = _relationFieldName;
        relationType = _relationType;
    }

    public Class<?> getTargetEntity() {
        return targetEntity;
    }

    public Class<?> getRelationEntity() {
        return relationEntity;
    }

    public String getRelationFieldName() {
        return relationFieldName;
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

        if (this.targetEntity != other.targetEntity) {
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
