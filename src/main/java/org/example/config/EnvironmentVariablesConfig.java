package org.example.config;

import org.example.exceptions.MissingEnvironmentVariableException;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class EnvironmentVariablesConfig {

    public static String getRequired(String key) {
        return Optional.ofNullable(System.getenv(key))
                .orElseThrow(() -> new MissingEnvironmentVariableException(key));
    }
}