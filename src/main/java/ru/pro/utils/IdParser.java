package ru.pro.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class IdParser {
    public UUID parse(String id) {
        try {
            return UUID.fromString(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный id: " + id);
        }
    }
}
