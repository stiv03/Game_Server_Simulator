package org.example.exceptions;

public class MissingEnvironmentVariableException extends RuntimeException {
    public MissingEnvironmentVariableException(String key) {
        super(ErrorMessages.format(ErrorMessages.MISSING_ENVIRONMENT_VARIABLE, key));
    }
}
