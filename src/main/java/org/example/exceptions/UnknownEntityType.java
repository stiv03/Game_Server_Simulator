package org.example.exceptions;

public class UnknownEntityType extends IllegalArgumentException{
    public UnknownEntityType(String name) {
        super(ErrorMessages.format(ErrorMessages.UNKNOWN_ENTITY_TYPE, name));
    }
}
