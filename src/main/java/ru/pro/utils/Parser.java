package ru.pro.utils;

import lombok.experimental.UtilityClass;
import ru.pro.model.enums.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
public class Parser {
    public static BigDecimal amountParse(String amount) {
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректная сумма: " + amount);
        }
    }

    public static UUID UUIDParse(String id) {
        try {
            return UUID.fromString(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный id: " + id);
        }
    }

    public static OperationType operationTypeParse(String type) {
        try {
            return OperationType.valueOf(type);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный тип операции: " + type);
        }
    }
}
