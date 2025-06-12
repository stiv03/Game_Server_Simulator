package org.example.exceptions;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String email) {
        super(ErrorMessages.format(ErrorMessages.USER_ALREADY_EXIST, email));
    }
}
