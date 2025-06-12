package org.example.persistence.service.utils;

import java.util.Optional;
import java.util.function.Consumer;

public class OptionalSetter {
    public static <T> void consumeIfPresent(Optional<T> optional, Consumer<T> consumer) {
        optional.ifPresent(consumer);
    }
}
