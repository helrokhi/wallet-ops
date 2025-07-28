package ru.pro.utils;

import org.springframework.stereotype.Component;
import ru.pro.model.enums.OperationType;

@Component
public class OperationTypeParser {
    public OperationType parse(String type) {
        try {
            return OperationType.valueOf(type);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректный тип операции: " + type);
        }
    }
}
