package ru.pro.model.dto;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import ru.pro.exception.InsufficientFundsException;

@Slf4j
public record ErrorEvent(String exceptionName, String message) {
    public void throwExceptionBasedOnError() {
        switch (exceptionName) {
            case "jakarta.persistence.EntityNotFoundException":
                throw new EntityNotFoundException(message);
            case "ru.pro.exception.InsufficientFundsException":
                throw new InsufficientFundsException(message);
            default:
                throw new RuntimeException("Неизвестная ошибка: " + message);
        }
    }
}
