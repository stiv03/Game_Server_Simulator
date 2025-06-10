package org.example.exceptions;

import java.text.MessageFormat;

public class ErrorMessages {

    public static final String MISSING_ENVIRONMENT_VARIABLE = "Required Environment variable {0} was not found.";

    public static String format(String message, Object... args) {
        return MessageFormat.format(message, args);
    }
}
