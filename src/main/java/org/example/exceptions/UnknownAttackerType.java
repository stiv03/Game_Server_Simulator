package org.example.exceptions;

public class UnknownAttackerType extends IllegalArgumentException{
    public UnknownAttackerType(String name) {
        super(ErrorMessages.format(ErrorMessages.UNKNOWN_ATTACKER_TYPE, name));
    }
}
