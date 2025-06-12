package org.example.persistence.mapper;

import org.example.config.messages.LogMessages;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DtoValidator {
    private static final Logger LOGGER = Logger.getLogger(DtoValidator.class.getName());

    private DtoValidator() {}

    public static <T> void validateDto(T dto, Supplier<? extends RuntimeException> exceptionSupplier, String response) {
        if (dto == null) {
            LOGGER.log(Level.SEVERE, LogMessages.INVALID_DTO, response);
            throw exceptionSupplier.get();
        }
    }

    public static void validateNotBlank(String value, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (value == null || value.isBlank()) {
            throw exceptionSupplier.get();
        }
    }
}
