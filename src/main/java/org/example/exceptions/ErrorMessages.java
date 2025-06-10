package org.example.exceptions;

import java.text.MessageFormat;

public class ErrorMessages {

    public static final String MISSING_ENVIRONMENT_VARIABLE = "Required Environment variable {0} was not found.";
    public static final String UNKNOWN_ATTACKER_TYPE = "Unknown attacker type: {0}";

    public static final String MISSING_EMAIL = "Email is required.";
    public static final String MISSING_USERNAME = "Username is required.";
    public static final String MISSING_PASSWORD = "Password is required.";

    public static final String INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String USER_ALREADY_EXIST = "A user with this credential: {0} already exists.";

    public static final String LOG_CONFIG_ERROR = "Error loading config file";
    public static final String WEBAPP_NOT_FOUND = "Web application directory not found: {0}";


    public static String format(String message, Object... args) {
        return MessageFormat.format(message, args);
    }
}
