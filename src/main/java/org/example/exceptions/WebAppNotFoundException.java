package org.example.exceptions;

public class WebAppNotFoundException extends RuntimeException {
    public WebAppNotFoundException(String key) {
        super(ErrorMessages.format(ErrorMessages.WEBAPP_NOT_FOUND, key));
    }
}