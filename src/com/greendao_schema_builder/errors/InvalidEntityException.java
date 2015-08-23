package com.greendao_schema_builder.errors;

public class InvalidEntityException extends Exception {

    public InvalidEntityException() {
    }

    public InvalidEntityException(String _message) {
        super(_message);
    }

    public InvalidEntityException(Throwable _cause) {
        super(_cause);
    }

    public InvalidEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}