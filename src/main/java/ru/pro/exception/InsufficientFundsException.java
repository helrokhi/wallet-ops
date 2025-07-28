package ru.pro.exception;

import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;

public class InsufficientFundsException extends ApiException {
    public InsufficientFundsException(String message) {
        super(
                CONFLICT.value(),
                "INSUFFICIENT_FUNDS",
                message,
                Map.of()
        );
    }
}
