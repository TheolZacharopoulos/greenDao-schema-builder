# GreenDao Schema Builder.

Builds a GreenDAO schema from a given domain model using java reflection.

## Schema Builder

A SchemaBuilder object can be used to generate a greenDao schema that is based on a domain model class, 
including relationships, interface implementations and super classes.
 
### How to use it
```
// The entity class is a model class.
Class<?> entityClass = Document.class;

// Set up the model
SchemaBuilder schemaBuilder = new SchemaBuilder(0.1, "com.example.schema", "./src/main/java");

// Exclude this static field.
schemaBuilder.addFieldToBlackList("ENTITY_NAME");

// Add an entity that is based on a model class.
schemaBuilder.addEntityProperty(
        new PropertyOptions(
            entityClass,                    // the entity Class.
            "code",                         // The primary key field name.
            entityClass,                    // Any super classes.
            "com.example.schemaIEntity")    // Any interfaces to be implemented.
    );

// Add a new relation One To One between Document and Transaction.
schemaBuilder.addEntityRelation(
        new EntityRelation(
                Document.class,
                Transaction.class,
                "transaction",
                EntityRelationType.ONE_TO_ONE));

// Generate the schema.
schemaBuilder.generate();
```
