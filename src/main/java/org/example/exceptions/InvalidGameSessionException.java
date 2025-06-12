package org.example.exceptions;

public class InvalidGameSessionException extends RuntimeException {
    public InvalidGameSessionException(String message) {
        super(message);
    }
}
