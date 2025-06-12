package org.example.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(ErrorMessages.format(ErrorMessages.INVALID_CREDENTIALS));
    }
}