package com.greendao_schema_builder.errors;

public class InvalidEntityRelationException extends Exception {

    public InvalidEntityRelationException() {
    }

    public InvalidEntityRelationException(String _message) {
        super(_message);
    }

    public InvalidEntityRelationException(Throwable _cause) {
        super(_cause);
    }

    public InvalidEntityRelationException(String message, Throwable cause) {
        super(message, cause);
    }
}