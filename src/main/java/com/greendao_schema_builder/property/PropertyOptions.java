package com.greendao_schema_builder.property;

/**
 * Argument object for the Build Entity Properties.
 */
public class PropertyOptions {
    public final static String PREFIX_ENTITY_NAME = "DB";
    private final Class<?> entityClass;
    private final String primaryKey;
    private final String superClassName;
    private final String[] interfaces;

    /**
     * Constructor.
     * @param _entityClass the class which will take the properties from.
     * @param _primaryKey the primary key field name.
     * @param _superClassName a super class name.
     * @param _interfaces a list of interfaces to be implemented (varargs)
     */
    public PropertyOptions(
            Class<?> _entityClass,
            String _primaryKey,
            String _superClassName,
            String ... _interfaces)
    {
        entityClass = _entityClass;
        primaryKey = _primaryKey;
        superClassName = _superClassName;
        interfaces = _interfaces;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public String[] getInterfaces() {
        return interfaces;
    }
}
